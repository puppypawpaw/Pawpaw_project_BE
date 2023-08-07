package com.puppy.pawpaw_project_be.application.auth.command;

import com.puppy.pawpaw_project_be.application.user.command.UserService;
import com.puppy.pawpaw_project_be.application.user.query.UserQuery;
import com.puppy.pawpaw_project_be.config.auth.application.TokenService;
import com.puppy.pawpaw_project_be.domain.auth.domain.OAuth2CustomUser;
import com.puppy.pawpaw_project_be.domain.auth.domain.Oauth2Provider;
import com.puppy.pawpaw_project_be.domain.auth.domain.TokenType;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignInRequest;
import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.auth.dto.response.TokenResponse;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import com.puppy.pawpaw_project_be.domain.user.dto.response.UserResponse;
import com.puppy.pawpaw_project_be.exception.user.NotFoundUserException;
import com.puppy.pawpaw_project_be.util.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SignService {
    private final UserService userService;
    private final UserQuery userQuery;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;
    private final String domain;
    private final String sameSite;
    private final UserRepository userRepository;

    public SignService(
        final UserService userService,
        final UserQuery userQuery,
        final AuthenticationManagerBuilder authenticationManagerBuilder,
        final TokenService tokenService,
        @Value("${custom.cookieDomain}") final String domain,
        @Value("${custom.sameSite}") final String sameSite,
        final UserRepository userRepository
    ) {
        this.userService = userService;
        this.userQuery = userQuery;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
        this.domain = domain;
        this.sameSite = sameSite;
        this.userRepository = userRepository;
    }

    @Transactional
    public void signUp(final SignUpRequest request) {
        userQuery.checkDuplication(request);
        userService.createUser(request);
    }

    @Transactional(readOnly = true)
    public UserResponse signIn(
        final HttpServletResponse response,
        final SignInRequest request
    ) {
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getId(), request.getPassword()));
        TokenResponse tokenResponse = tokenService.createTokenResponse(authenticate);
        tokenService.saveRefreshToken(UserId.of(authenticate.getName()), tokenResponse.getRefreshToken());

        CookieUtil.addCookie(
            response,
            TokenType.ACCESS.name(),
            tokenResponse.getAccessToken(),
            tokenResponse.getAccessTokenLifeTime() / 1000,
            domain,
            sameSite
        );

        CookieUtil.addCookie(
            response,
            TokenType.REFRESH.name(),
            tokenResponse.getRefreshToken(),
            tokenResponse.getRefreshTokenLifeTime() / 1000,
            domain,
            sameSite
        );

        return getUserInfo(request);
    }

    @Transactional(readOnly = true)
    public void signIn(
        final HttpServletResponse response,
        final Authentication authenticate
    ) {
        TokenResponse tokenResponse = tokenService.createTokenResponse(authenticate);

        UserId userId = userRepository.findByIdAndProvider(((OAuth2CustomUser)authenticate.getPrincipal()).getEmail(),
                Oauth2Provider.valueOf(authenticate.getName().toUpperCase()))
            .map(User::getUserId)
            .orElseThrow(NotFoundUserException::new);

        tokenService.saveRefreshToken(userId, tokenResponse.getRefreshToken());

        CookieUtil.addCookie(
            response,
            TokenType.ACCESS.name(),
            tokenResponse.getAccessToken(),
            tokenResponse.getAccessTokenLifeTime() / 1000,
            domain,
            sameSite
        );

        CookieUtil.addCookie(
            response,
            TokenType.REFRESH.name(),
            tokenResponse.getRefreshToken(),
            tokenResponse.getRefreshTokenLifeTime() / 1000,
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
