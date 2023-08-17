package kr.co.pawpaw.api.application.auth;

import kr.co.pawpaw.api.config.auth.provider.JwtTokenProvider;
import kr.co.pawpaw.api.config.property.CookieProperties;
import kr.co.pawpaw.api.config.property.JwtProperties;
import kr.co.pawpaw.api.dto.auth.SignInRequest;
import kr.co.pawpaw.api.dto.auth.SignUpRequest;
import kr.co.pawpaw.api.dto.auth.SocialSignUpRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import kr.co.pawpaw.common.exception.auth.InvalidOAuth2TempKeyException;
import kr.co.pawpaw.common.exception.auth.NotEqualPasswordConfirmException;
import kr.co.pawpaw.common.exception.term.NotAgreeAllRequiredTermException;
import kr.co.pawpaw.common.exception.user.DuplicateIdException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.common.util.CookieUtil;
import kr.co.pawpaw.domainrdb.pet.service.command.PetCommand;
import kr.co.pawpaw.domainrdb.term.domain.UserTermAgree;
import kr.co.pawpaw.domainrdb.term.service.command.TermCommand;
import kr.co.pawpaw.domainrdb.term.service.query.TermQuery;
import kr.co.pawpaw.domainrdb.user.domain.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.command.UserCommand;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.domainredis.auth.domain.RefreshToken;
import kr.co.pawpaw.domainredis.auth.domain.TokenType;
import kr.co.pawpaw.domainredis.auth.service.command.RefreshTokenCommand;
import kr.co.pawpaw.domainredis.auth.service.query.OAuth2TempAttributesQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserQuery userQuery;
    private final UserCommand userCommand;
    private final TermQuery termQuery;
    private final TermCommand termCommand;
    private final PetCommand petCommand;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenCommand refreshTokenCommand;
    private final CookieProperties cookieProperties;
    private final OAuth2TempAttributesQuery oAuth2TempAttributesQuery;

    @Transactional
    public void signUp(
        final SignUpRequest request,
        final MultipartFile image
    ) {
        // 유저 중복 확인(아이디 등등)
        if (userQuery.existsById(request.getEmail())) {
            throw new DuplicateIdException();
        }

        // 약관 동의 유효성 확인(required 인거 모두 다 동의 했는지)
        if (!termQuery.isAllRequiredTermIds(new HashSet<>(request.getTermAgrees()))) {
            throw new NotAgreeAllRequiredTermException();
        }

        // 비밀번호 확인 유효성 확인
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new NotEqualPasswordConfirmException();
        }

        //TODO 여기서 객체 스토리지 저장 처리 후 userService에 객체 엔티티를 넘겨줌

        //TODO 여기서는 받은 객체로 userImage entity를 만든다.

        User user = userCommand.save(request.toUser(passwordEncoder.encode(request.getPassword())));
        petCommand.saveAll(request.toPet(user));

        // 약관 동의 추가
        Collection<UserTermAgree> userTermAgrees = termQuery.findAllByOrderIsIn(request.getTermAgrees())
            .stream()
            .map(term -> UserTermAgree.builder()
                .term(term)
                .user(user)
                .build())
            .collect(Collectors.toList());

        termCommand.saveAllUserTermAgrees(userTermAgrees);
    }

    @Transactional
    public void socialSignUp(
        final SocialSignUpRequest request,
        final MultipartFile image
    ) {
        // 약관 동의 유효성 확인(required 인거 모두 다 동의 했는지)
        if (!termQuery.isAllRequiredTermIds(new HashSet<>(request.getTermAgrees()))) {
            throw new NotAgreeAllRequiredTermException();
        }

        // 여기서 OAuth2TempValue 가져와서 userService에 전달해줌
        OAuth2TempAttributes oAuth2TempAttributes = oAuth2TempAttributesQuery.findById(request.getKey())
            .orElseThrow(InvalidOAuth2TempKeyException::new);

        // user 등록
        User user = userCommand.save(request.toUser(
            oAuth2TempAttributes.getEmail(),
            OAuth2Provider.valueOf(oAuth2TempAttributes.getProvider())
        ));

        // pet 등록
        petCommand.saveAll(request.toPet(user));

        // 약관 동의 추가
        Collection<UserTermAgree> userTermAgrees = termQuery.findAllByOrderIsIn(request.getTermAgrees())
            .stream()
            .map(term -> UserTermAgree.builder()
                .term(term)
                .user(user)
                .build())
            .collect(Collectors.toList());

        termCommand.saveAllUserTermAgrees(userTermAgrees);
    }

    @Transactional(readOnly = true)
    public UserResponse signIn(
        final HttpServletResponse response,
        final SignInRequest request
    ) {
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        signIn(response, authenticate);

        return userQuery.findByEmail(request.getEmail())
                .map(UserResponse::of)
                .orElseThrow(NotFoundUserException::new);
    }

    public void signIn(
        final HttpServletResponse response,
        final Authentication authenticate
    ) {
        String accessTokenValue = jwtTokenProvider.createAccessToken(authenticate);
        String refreshTokenValue = jwtTokenProvider.createRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
            .userId(authenticate.getName())
            .value(refreshTokenValue)
            .timeout(jwtProperties.getRefreshTokenLifeTime() / 1000)
            .build();

        refreshTokenCommand.save(refreshToken);

        CookieUtil.addCookie(
            response,
            TokenType.ACCESS.name(),
            accessTokenValue,
            (int) ((jwtProperties.getAccessTokenLifeTime() / 1000)),
            cookieProperties.getDomain()
        );

        CookieUtil.addCookie(
            response,
            TokenType.REFRESH.name(),
            refreshTokenValue,
            (int) ((jwtProperties.getRefreshTokenLifeTime() / 1000)),
            cookieProperties.getDomain()
        );
    }

    public void signOut(
        final UserId userId,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        CookieUtil.deleteCookie(
            request,
            response,
            TokenType.ACCESS.name()
        );

        CookieUtil.deleteCookie(
            request,
            response,
            TokenType.REFRESH.name()
        );

        refreshTokenCommand.deleteById(userId.getValue());
    }

    public UserResponse whoAmI(final UserId userId) {
        return userQuery.findByUserId(userId)
            .map(UserResponse::of)
            .orElseThrow(NotFoundUserException::new);
    }
}
