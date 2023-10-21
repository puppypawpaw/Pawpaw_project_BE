package kr.co.pawpaw.api.service.auth;

import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.dto.auth.DuplicateEmailResponse;
import kr.co.pawpaw.api.dto.auth.SignUpRequest;
import kr.co.pawpaw.api.dto.auth.SocialSignUpInfoResponse;
import kr.co.pawpaw.api.dto.auth.SocialSignUpRequest;
import kr.co.pawpaw.api.util.file.FileUtil;
import kr.co.pawpaw.api.util.user.UserUtil;
import kr.co.pawpaw.common.exception.auth.DuplicateEmailException;
import kr.co.pawpaw.common.exception.auth.DuplicatePhoneNumberException;
import kr.co.pawpaw.common.exception.auth.InvalidOAuth2TempKeyException;
import kr.co.pawpaw.common.exception.auth.NotVerifiedPhoneNumberException;
import kr.co.pawpaw.common.exception.term.NotAgreeAllRequiredTermException;
import kr.co.pawpaw.mysql.pet.service.command.PetCommand;
import kr.co.pawpaw.mysql.sms.domain.SmsUsagePurpose;
import kr.co.pawpaw.mysql.storage.service.query.FileQuery;
import kr.co.pawpaw.mysql.term.domain.UserTermAgree;
import kr.co.pawpaw.mysql.term.service.command.TermCommand;
import kr.co.pawpaw.mysql.term.service.query.TermQuery;
import kr.co.pawpaw.mysql.user.domain.OAuth2Provider;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.command.UserCommand;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.redis.auth.domain.VerifiedPhoneNumber;
import kr.co.pawpaw.redis.auth.service.command.OAuth2TempAttributesCommand;
import kr.co.pawpaw.redis.auth.service.query.OAuth2TempAttributesQuery;
import kr.co.pawpaw.redis.auth.service.query.VerifiedPhoneNumberQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final TermQuery termQuery;
    private final TermCommand termCommand;
    private final PetCommand petCommand;
    private final VerifiedPhoneNumberQuery verifiedPhoneNumberQuery;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2TempAttributesQuery oAuth2TempAttributesQuery;
    private final OAuth2TempAttributesCommand oAuth2TempAttributesCommand;
    private final FileService fileService;
    private final FileQuery fileQuery;

    @Transactional
    public void signUp(
        final SignUpRequest request,
        final MultipartFile image
    ) {
        validateRequest(request);

        String userName = getUserName(request);
        User user = createUser(request, userName);

        if (image != null && FileUtil.getByteLength(image) > 0) {
            saveUserImageByMultipartFile(image, user);
        } else {
            saveDefaultUserImage(user);
        }

        petCommand.saveAll(request.toPet(user));
        saveUserTermAgreements(request.getTermAgrees(), user);
    }

    private String getUserName(final SignUpRequest request) {
        return verifiedPhoneNumberQuery.findByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name())
            .map(VerifiedPhoneNumber::getUserName)
            .orElseThrow(NotVerifiedPhoneNumberException::new);
    }

    @Transactional
    public UserId socialSignUp(
        final SocialSignUpRequest request,
        final MultipartFile image
    ) {
        validateRequiredTermAgreed(request.getTermAgrees());
        OAuth2TempAttributes oAuth2TempAttributes = getOAuth2TempAttributes(request.getKey());

        User user = createUser(request, oAuth2TempAttributes);

        if (!request.getNoImage()) {
            saveUserImageByMultipartFileOrUrl(image, oAuth2TempAttributes.getProfileImageUrl(), user);
        } else {
            saveDefaultUserImage(user);
        }

        petCommand.saveAll(request.toPet(user));
        saveUserTermAgreements(request.getTermAgrees(), user);
        deleteOAuth2TempAttributes(oAuth2TempAttributes);

        return user.getUserId();
    }

    @Transactional(readOnly = true)
    public DuplicateEmailResponse checkDuplicateEmail(final String email) {
        return DuplicateEmailResponse.of(userQuery.existsByEmailAndProvider(email, null));
    }

    public SocialSignUpInfoResponse getOAuth2SignUpTempInfo(final String key) {
        return oAuth2TempAttributesQuery.findById(key)
            .map(SocialSignUpInfoResponse::of)
            .orElseThrow(InvalidOAuth2TempKeyException::new);
    }

    private void saveUserImageByMultipartFileOrUrl(
        final MultipartFile image,
        final String url,
        final User user
    ) {
        if (image != null && FileUtil.getByteLength(image) > 0) {
            saveUserImageByMultipartFile(image, user);
        } else {
            saveUserImageByUrl(url, user);
        }
    }

    private void deleteOAuth2TempAttributes(final OAuth2TempAttributes oAuth2TempAttributes) {
        oAuth2TempAttributesCommand.deleteById(oAuth2TempAttributes.getKey());
    }

    private void validateRequest(final SignUpRequest request) {
        request.deletePhoneNumberHyphen();
        validateDuplicateEmail(request.getEmail());
        validateRequiredTermAgreed(request.getTermAgrees());
        validatePhoneNumber(request.getPhoneNumber());
    }

    private void saveUserImageByMultipartFile(
        final MultipartFile image,
        final User user
    ) {
        user.updateImage(fileService.saveFileByMultipartFile(image, user.getUserId()));
    }

    private void saveDefaultUserImage(
        final User user
    ) {
        user.updateImage(fileQuery.getReferenceById(UserUtil.getUserDefaultImageName()));
    }

    private void saveUserImageByUrl(
        final String url,
        final User user
    ) {
        user.updateImage(fileService.saveFileByUrl(url, user.getUserId()));
    }

    private User createUser(
        final SocialSignUpRequest request,
        final OAuth2TempAttributes oAuth2TempAttributes
    ) {
        return userCommand.save(request.toUser(
            oAuth2TempAttributes.getEmail(),
            OAuth2Provider.valueOf(oAuth2TempAttributes.getProvider())
        ));
    }

    private OAuth2TempAttributes getOAuth2TempAttributes(final String key) {
        return oAuth2TempAttributesQuery.findById(key)
            .orElseThrow(InvalidOAuth2TempKeyException::new);
    }

    private void saveUserTermAgreements(
        final List<Long> termAgrees,
        final User user
    ) {
        Collection<UserTermAgree> userTermAgrees = termQuery.findAllByOrderIsIn(termAgrees)
            .stream()
            .map(term -> UserTermAgree.builder()
                .term(term)
                .user(user)
                .build())
            .collect(Collectors.toList());

        termCommand.saveAllUserTermAgrees(userTermAgrees);
    }

    private User createUser(
        final SignUpRequest request,
        final String userName
    ) {
        return userCommand.save(request.toUser(
            passwordEncoder.encode(request.getPassword()),
            userName
        ));
    }

    private void validateRequiredTermAgreed(final Collection<Long> termAgreed) {
        if (!termQuery.isAllRequiredTermIds(new HashSet<>(termAgreed))) {
            throw new NotAgreeAllRequiredTermException();
        }
    }

    private void validateDuplicateEmail(final String email) {
        if (userQuery.existsByEmailAndProvider(email, null)) {
            throw new DuplicateEmailException();
        }
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if (userQuery.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatePhoneNumberException();
        }

        if (!verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(phoneNumber, SmsUsagePurpose.SIGN_UP.name())) {
            throw new NotVerifiedPhoneNumberException();
        }
    }
}
