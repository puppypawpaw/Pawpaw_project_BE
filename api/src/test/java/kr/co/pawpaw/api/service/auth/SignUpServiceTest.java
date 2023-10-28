package kr.co.pawpaw.api.service.auth;

import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.dto.auth.DuplicateEmailResponse;
import kr.co.pawpaw.api.dto.auth.SignUpRequest;
import kr.co.pawpaw.api.dto.auth.SocialSignUpInfoResponse;
import kr.co.pawpaw.api.dto.auth.SocialSignUpRequest;
import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.position.PositionRequest;
import kr.co.pawpaw.api.util.user.UserUtil;
import kr.co.pawpaw.common.exception.auth.DuplicateEmailException;
import kr.co.pawpaw.common.exception.auth.DuplicatePhoneNumberException;
import kr.co.pawpaw.common.exception.auth.InvalidOAuth2TempKeyException;
import kr.co.pawpaw.common.exception.auth.NotVerifiedPhoneNumberException;
import kr.co.pawpaw.common.exception.term.NotAgreeAllRequiredTermException;
import kr.co.pawpaw.mysql.pet.domain.Pet;
import kr.co.pawpaw.mysql.pet.domain.PetType;
import kr.co.pawpaw.mysql.pet.service.command.PetCommand;
import kr.co.pawpaw.mysql.position.Position;
import kr.co.pawpaw.mysql.sms.domain.SmsUsagePurpose;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.service.query.FileQuery;
import kr.co.pawpaw.mysql.term.domain.Term;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {
    @Mock
    private UserQuery userQuery;
    @Mock
    private UserCommand userCommand;
    @Mock
    private TermQuery termQuery;
    @Mock
    private TermCommand termCommand;
    @Mock
    private PetCommand petCommand;
    @Mock
    private VerifiedPhoneNumberQuery verifiedPhoneNumberQuery;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private FileService fileService;
    @Mock
    private OAuth2TempAttributesQuery oAuth2TempAttributesQuery;
    @Mock
    private OAuth2TempAttributesCommand oAuth2TempAttributesCommand;
    @Mock
    private FileQuery fileQuery;

    @InjectMocks
    private SignUpService signUpService;

    @Nested
    @DisplayName("일반 회원가입 메서드는")
    class SignUp {
        List<Long> termAgrees = List.of(1L, 2L, 3L);
        SignUpRequest request = SignUpRequest.builder()
            .email("abc@test.com")
            .password("password")
            .nickname("nickname")
            .position(PositionRequest.builder()
                .name("position")
                .latitude(13.4)
                .longitude(13.5)
                .build())
            .phoneNumber("01012345678")
            .termAgrees(termAgrees)
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .build();
        String passwordEncoded = "passwordEncoded";
        VerifiedPhoneNumber vPhoneNo = VerifiedPhoneNumber.builder()
            .phoneNumber(request.getPhoneNumber())
            .usagePurpose(SmsUsagePurpose.SIGN_UP.name())
            .userName("username")
            .build();

        User savedUser = request.toUser(passwordEncoded, vPhoneNo.getUserName());
        List<Term> agreeTermList = termAgrees.stream()
            .map(termOrder -> Term.builder()
                .title(String.format("약관 %s", termOrder))
                .content(String.format("약관 %s 내용", termOrder))
                .order(termOrder)
                .required(termOrder < 4)
                .build())
            .collect(Collectors.toList());

        File file = File.builder()
            .fileName("기본 이미지")
            .build();

        File newFile = File.builder()
            .fileName("새로운 이미지")
            .build();

        @Test
        @DisplayName("이미 가입한 이메일이면 예외를 발생한다.")
        void duplicateEmailException() {
            //given
            when(userQuery.existsByEmailAndProvider(request.getEmail(), null)).thenReturn(true);

            //then
            assertThatThrownBy(() -> signUpService.signUp(request, multipartFile)).isInstanceOf(DuplicateEmailException.class);
        }

        @Test
        @DisplayName("필수 약관을 하나라도 동의안하면 예외가 발생한다.")
        void notAgreeAllRequiredTermException() {
            //given
            when(userQuery.existsByEmailAndProvider(request.getEmail(), null)).thenReturn(false);
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(false);

            //then
            assertThatThrownBy(() -> signUpService.signUp(request, multipartFile)).isInstanceOf(NotAgreeAllRequiredTermException.class);
        }

        @Test
        @DisplayName("이미 가입한 핸드폰 번호이면 예외가 발생한다.")
        void duplicatePhoneNumberException() {
            //given
            when(userQuery.existsByEmailAndProvider(request.getEmail(), null)).thenReturn(false);
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(userQuery.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

            //when
            assertThatThrownBy(() -> signUpService.signUp(request, multipartFile)).isInstanceOf(DuplicatePhoneNumberException.class);
        }

        @Test
        @DisplayName("인증받지 않은 핸드폰 번호면 예외가 발생한다.")
        void notVerifiedPhoneNumberException() {
            //given
            when(userQuery.existsByEmailAndProvider(request.getEmail(), null)).thenReturn(false);
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(userQuery.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name())).thenReturn(false);

            //then
            assertThatThrownBy(() -> signUpService.signUp(request, multipartFile)).isInstanceOf(NotVerifiedPhoneNumberException.class);
        }

        @Test
        @DisplayName("이미지가 null이면 기본 이미지를 유저의 이미지로 설정한다.")
        void ifImageIsNullSetDefaultImageToUserImage() {
            //given
            when(userQuery.existsByEmailAndProvider(request.getEmail(), null)).thenReturn(false);
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(userQuery.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name())).thenReturn(true);
            when(verifiedPhoneNumberQuery.findByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name())).thenReturn(Optional.of(vPhoneNo));
            when(passwordEncoder.encode(request.getPassword())).thenReturn(passwordEncoded);
            when(userCommand.save(any(User.class))).thenReturn(savedUser);
            when(termQuery.findAllByOrderIsIn(termAgrees)).thenReturn(agreeTermList);
            when(fileQuery.getReferenceById(UserUtil.getUserDefaultImageName())).thenReturn(file);

            //when
            signUpService.signUp(request, null);

            //then
            assertThat(savedUser.getUserImage()).usingRecursiveComparison().isEqualTo(file);
        }

        @Test
        @DisplayName("이미지가 null이 아니면 multipartfile로 생성한 이미지를 유저의 이미지로 설정한다.")
        void ifImageNotNullSetMultipartImageToUserImage() throws IOException {
            //given
            when(userQuery.existsByEmailAndProvider(request.getEmail(), null)).thenReturn(false);
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(userQuery.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
            when(verifiedPhoneNumberQuery.existsByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name())).thenReturn(true);
            when(verifiedPhoneNumberQuery.findByPhoneNumberAndUsagePurpose(request.getPhoneNumber(), SmsUsagePurpose.SIGN_UP.name())).thenReturn(Optional.of(vPhoneNo));
            when(passwordEncoder.encode(request.getPassword())).thenReturn(passwordEncoded);
            when(userCommand.save(any(User.class))).thenReturn(savedUser);
            when(termQuery.findAllByOrderIsIn(termAgrees)).thenReturn(agreeTermList);
            when(fileService.saveFileByMultipartFile(multipartFile, savedUser.getUserId())).thenReturn(newFile);
            when(multipartFile.getBytes()).thenReturn(new byte[] {Byte.parseByte("123")});

            //when
            signUpService.signUp(request, multipartFile);

            //then
            assertThat(savedUser.getUserImage()).usingRecursiveComparison().isEqualTo(newFile);
        }
    }

    @Nested
    @DisplayName("소셜 회원가입 메서드는")
    class SocialSignUp {
        List<Long> termAgrees = List.of(1L, 2L, 3L);
        SocialSignUpRequest noImageRequest = SocialSignUpRequest.builder()
            .termAgrees(termAgrees)
            .key("temp key")
            .noImage(true)
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .position(PositionRequest.builder()
                .latitude(36.8)
                .longitude(36.8)
                .name("위치")
                .build())
            .build();

        SocialSignUpRequest yesImageRequest = SocialSignUpRequest.builder()
            .termAgrees(termAgrees)
            .key("temp key")
            .noImage(false)
            .petInfos(List.of(
                CreatePetRequest.builder()
                    .petName("루이")
                    .petType(PetType.DOG)
                    .build()
            ))
            .position(PositionRequest.builder()
                .latitude(36.8)
                .longitude(36.8)
                .name("위치")
                .build())
            .build();

        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .name("이름")
            .provider("GOOGLE")
            .profileImageUrl("http://example.com")
            .email("email@example.com")
            .build();
        File file = File.builder()
            .fileName("기본 이미지")
            .build();
        User savedUser = noImageRequest.toUser(oAuth2TempAttributes.getEmail(), OAuth2Provider.valueOf(oAuth2TempAttributes.getProvider()));
        List<Term> agreeTermList = termAgrees.stream()
            .map(termOrder -> Term.builder()
                .title(String.format("약관 %s", termOrder))
                .content(String.format("약관 %s 내용", termOrder))
                .order(termOrder)
                .required(termOrder < 4)
                .build())
            .collect(Collectors.toList());

        @Test
        @DisplayName("필수 약관을 하나라도 동의하지 않은 경우에는 예외가 발생한다.")
        void notAgreeAllRequiredTermException() {
            //given
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(false);

            //then
            assertThatThrownBy(() -> signUpService.socialSignUp(noImageRequest, multipartFile)).isInstanceOf(NotAgreeAllRequiredTermException.class);
        }

        @Test
        @DisplayName("입력받은 소셜회원가입 임시 키가 유효하지 않으면 예외가 발생한다.")
        void InvalidOAuth2TempKeyException() {
            //given
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(oAuth2TempAttributesQuery.findById(noImageRequest.getKey())).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> signUpService.socialSignUp(noImageRequest, multipartFile)).isInstanceOf(InvalidOAuth2TempKeyException.class);
        }

        @Test
        @DisplayName("noImage 필드가 true이면 기본 이미지를 유저의 이미지로 설정한다.")
        void ifNoImageIsTrueSetDefaultImageToUserImage() {
            //given
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(oAuth2TempAttributesQuery.findById(noImageRequest.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));
            when(userCommand.save(any(User.class))).thenReturn(savedUser);
            when(fileQuery.getReferenceById(UserUtil.getUserDefaultImageName())).thenReturn(file);
            when(termQuery.findAllByOrderIsIn(termAgrees)).thenReturn(agreeTermList);

            //when
            UserId userId = signUpService.socialSignUp(noImageRequest, multipartFile);

            //then
            assertThat(userId).isEqualTo(savedUser.getUserId());
            assertThat(savedUser.getUserImage()).isEqualTo(file);
        }

        @Test
        @DisplayName("noImage 필드가 false이고 multipartfile가 null이 아니면 multipartfile로 생성한 이미지를 유저의 이미지로 설정한다.")
        void ifNoImageIsFalseAndMultipartNotNullSetMultipartImageToUserImage() throws IOException {
            //given
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(oAuth2TempAttributesQuery.findById(yesImageRequest.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));
            when(userCommand.save(any(User.class))).thenReturn(savedUser);
            when(fileService.saveFileByMultipartFile(multipartFile, savedUser.getUserId())).thenReturn(file);
            when(termQuery.findAllByOrderIsIn(termAgrees)).thenReturn(agreeTermList);
            when(multipartFile.getBytes()).thenReturn(new byte[] {Byte.parseByte("123")});

            //when
            UserId userId = signUpService.socialSignUp(yesImageRequest, multipartFile);

            //then
            assertThat(userId).isEqualTo(savedUser.getUserId());
            assertThat(savedUser.getUserImage()).isEqualTo(file);
        }

        @Test
        @DisplayName("noImage 필드가 false이고 multipartfile가 null이면 소셜 로그인 시에 제공받은 이미지 url로 생성한 이미지를 유저의 이미지로 설정한다.")
        void ifNoImageIsFalseAndMultipartIsNullSetMultipartImageToUserImage() throws IOException {
            //given
            when(termQuery.isAllRequiredTermIds(new HashSet<>(termAgrees))).thenReturn(true);
            when(oAuth2TempAttributesQuery.findById(yesImageRequest.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));
            when(userCommand.save(any(User.class))).thenReturn(savedUser);
            when(fileService.saveFileByUrl(oAuth2TempAttributes.getProfileImageUrl(), savedUser.getUserId())).thenReturn(file);
            when(termQuery.findAllByOrderIsIn(termAgrees)).thenReturn(agreeTermList);

            //when
            UserId userId = signUpService.socialSignUp(yesImageRequest, null);

            //then
            assertThat(userId).isEqualTo(savedUser.getUserId());
            assertThat(savedUser.getUserImage()).isEqualTo(file);
        }
    }

    @Test
    @DisplayName("이메일 중복회원가입 테스트")
    void checkDuplicateEmailTest() {
        //given
        String email = "hello@email.com";

        when(userQuery.existsByEmailAndProvider(email, null)).thenReturn(true);
        //when
        DuplicateEmailResponse response = signUpService.checkDuplicateEmail(email);

        //then
        verify(userQuery).existsByEmailAndProvider(email, null);
        assertThat(response.isDuplicate()).isTrue();
    }

    @Test
    @DisplayName("소셜 회원가입 임시정보 테스트")
    void getOAuth2SignUpTempInfo() {
        //given
        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .email("hello@email.com")
            .provider("GOOGLE")
            .profileImageUrl("http://example.com")
            .name("name")
            .build();

        when(oAuth2TempAttributesQuery.findById(oAuth2TempAttributes.getKey())).thenReturn(Optional.of(oAuth2TempAttributes));

        //when
        SocialSignUpInfoResponse response = signUpService.getOAuth2SignUpTempInfo(oAuth2TempAttributes.getKey());
        assertThatThrownBy(() -> signUpService.getOAuth2SignUpTempInfo("123")).isInstanceOf(InvalidOAuth2TempKeyException.class);

        //then
        assertThat(response.getName()).isEqualTo(oAuth2TempAttributes.getName());
        assertThat(response.getProfileImageUrl()).isEqualTo(oAuth2TempAttributes.getProfileImageUrl());
    }
}