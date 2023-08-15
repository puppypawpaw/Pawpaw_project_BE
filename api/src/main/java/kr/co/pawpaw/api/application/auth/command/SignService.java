package kr.co.pawpaw.api.application.auth.command;

import kr.co.pawpaw.api.application.pet.command.PetService;
import kr.co.pawpaw.api.application.term.command.TermService;
import kr.co.pawpaw.api.application.term.query.TermQuery;
import kr.co.pawpaw.api.application.user.command.UserService;
import kr.co.pawpaw.api.application.user.query.UserQuery;
import kr.co.pawpaw.api.config.auth.application.TokenService;
import kr.co.pawpaw.common.exception.auth.NotEqualPasswordConfirmException;
import kr.co.pawpaw.common.exception.pet.InvalidPetIntroductionException;
import kr.co.pawpaw.common.exception.pet.InvalidPetNameException;
import kr.co.pawpaw.common.exception.term.NotAgreeAllRequiredTermException;

import kr.co.pawpaw.common.util.CookieUtil;
import kr.co.pawpaw.domainrdb.auth.dto.request.SignInRequest;
import kr.co.pawpaw.domainrdb.auth.dto.request.SignUpRequest;

import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.repository.UserRepository;
import kr.co.pawpaw.domainrdb.user.dto.response.UserResponse;
import kr.co.pawpaw.domainredis.auth.domain.TokenType;
import kr.co.pawpaw.domainredis.auth.dto.response.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

@Service
public class SignService {
    private final UserService userService;
    private final UserQuery userQuery;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;
    private final String domain;
    private final String sameSite;
    private final UserRepository userRepository;
    private final TermQuery termQuery;
    private final TermService termService;
    private final PetService petService;
    private static final Long PET_NAME_LENGTH_LIMIT = 10L;
    private static final Long PET_INTRODUCTION_LENGTH_LIMIT = 50L;

    public SignService(
        final UserService userService,
        final UserQuery userQuery,
        final AuthenticationManagerBuilder authenticationManagerBuilder,
        final TokenService tokenService,
        @Value("${custom.cookieDomain}") final String domain,
        @Value("${custom.sameSite}") final String sameSite,
        final UserRepository userRepository,
        final TermQuery termQuery,
        final TermService termService,
        final PetService petService
    ) {
        this.userService = userService;
        this.userQuery = userQuery;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
        this.domain = domain;
        this.sameSite = sameSite;
        this.userRepository = userRepository;
        this.termQuery = termQuery;
        this.termService = termService;
        this.petService = petService;
    }

    @Transactional
    public void signUp(
        final SignUpRequest request,
        final MultipartFile petImage
    ) {
        // 유저 중복 확인(아이디 등등)
        userQuery.checkDuplication(request);

        // 약관 동의 유효성 확인(required 인거 모두 다 동의 했는지)
        if (!termQuery.isAllRequiredTermIds(new HashSet<>(request.getTermAgrees()))) {
            throw new NotAgreeAllRequiredTermException();
        }

        // 비밀번호 확인 유효성 확인
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new NotEqualPasswordConfirmException();
        }

        // 반려동물 이름 길이 확인
        if (request.getPetName().length() > PET_NAME_LENGTH_LIMIT) {
            throw new InvalidPetNameException();
        }

        // 반려동물 소개 길이 확인
        if (request.getPetIntroduction().length() > PET_INTRODUCTION_LENGTH_LIMIT) {
            throw new InvalidPetIntroductionException();
        }

        User user = userService.createUser(request);
        petService.createPet(request, petImage, user);

        // 약관 동의 추가
        termService.createTermAgree(request.getTermAgrees(), user);
    }

    @Transactional(readOnly = true)
    public UserResponse signIn(
        final HttpServletResponse response,
        final SignInRequest request
    ) {
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getId(), request.getPassword()));

        signIn(response, authenticate);

        return getUserInfo(request);
    }

    @Transactional(readOnly = true)
    public void signIn(
        final HttpServletResponse response,
        final Authentication authenticate
    ) {
        TokenResponse tokenResponse = tokenService.createTokenResponse(authenticate);
        tokenService.saveRefreshToken(UserId.of(authenticate.getName()), tokenResponse.getRefreshToken());

        CookieUtil.addCookie(
            response,
            TokenType.ACCESS.name(),
            tokenResponse.getAccessToken(),
            (int) ((tokenResponse.getAccessTokenLifeTime() / 1000)),
            domain,
            sameSite
        );

        CookieUtil.addCookie(
            response,
            TokenType.REFRESH.name(),
            tokenResponse.getRefreshToken(),
            (int) ((tokenResponse.getRefreshTokenLifeTime() / 1000)),
            domain,
            sameSite
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
            TokenType.ACCESS.name(),
            domain,
            sameSite
        );

        CookieUtil.deleteCookie(
            request,
            response,
            TokenType.REFRESH.name(),
            domain,
            sameSite
        );

        tokenService.deleteRefreshToken(userId.getValue());
    }

    private UserResponse getUserInfo(final SignInRequest request) {
        return UserResponse.of(
            userRepository.findById(request.getId())
                .orElseThrow(NotFoundUserException::new));
    }
}
