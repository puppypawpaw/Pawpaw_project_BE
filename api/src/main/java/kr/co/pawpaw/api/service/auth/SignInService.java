package kr.co.pawpaw.api.service.auth;

import kr.co.pawpaw.api.config.auth.object.OAuth2CustomUser;
import kr.co.pawpaw.api.config.auth.provider.JwtTokenProvider;
import kr.co.pawpaw.api.config.property.CookieProperties;
import kr.co.pawpaw.api.config.property.JwtProperties;
import kr.co.pawpaw.api.dto.auth.SignInRequest;
import kr.co.pawpaw.api.util.cookie.CookieUtil;
import kr.co.pawpaw.mysql.user.domain.Role;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.redis.auth.domain.RefreshToken;
import kr.co.pawpaw.redis.auth.domain.TokenType;
import kr.co.pawpaw.redis.auth.service.command.RefreshTokenCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignInService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenCommand refreshTokenCommand;
    private final CookieProperties cookieProperties;

    public void signIn(
        final HttpServletResponse response,
        final SignInRequest request
    ) {
        Authentication authenticate = authenticateUser(request.getEmail(), request.getPassword());

        String accessTokenValue = createAccessToken(authenticate);
        String refreshTokenValue = createRefreshToken();

        saveRefreshToken(authenticate.getName(), refreshTokenValue);

        addTokenCookie(response, TokenType.ACCESS, accessTokenValue, jwtProperties.getAccessTokenLifeTime());
        addTokenCookie(response, TokenType.REFRESH, refreshTokenValue, jwtProperties.getRefreshTokenLifeTime());
    }

    public void socialSignIn(
        final HttpServletResponse response,
        final UserId userId
    ) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(Role.USER.getAuthority()));
        OAuth2CustomUser oAuth2CustomUser = new OAuth2CustomUser(
            null,
            authorities,
            userId
        );
        Authentication authenticate = new UsernamePasswordAuthenticationToken(oAuth2CustomUser, null, authorities);
        String accessTokenValue = createAccessToken(authenticate);
        String refreshTokenValue = createRefreshToken();

        saveRefreshToken(authenticate.getName(), refreshTokenValue);

        addTokenCookie(response, TokenType.ACCESS, accessTokenValue, jwtProperties.getAccessTokenLifeTime());
        addTokenCookie(response, TokenType.REFRESH, refreshTokenValue, jwtProperties.getRefreshTokenLifeTime());
    }

    private Authentication authenticateUser(
        final String email,
        final String password
    ) {
        return authenticationManagerBuilder.getObject()
            .authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    private String createAccessToken(final Authentication authentication) {
        return jwtTokenProvider.createAccessToken(authentication);
    }

    private String createRefreshToken() {
        return jwtTokenProvider.createRefreshToken();
    }

    private void saveRefreshToken(
        final String userId,
        final String tokenValue
    ) {
        refreshTokenCommand.save(RefreshToken.builder()
            .userId(userId)
            .value(tokenValue)
            .build());
    }

    private void addTokenCookie(
        final HttpServletResponse response,
        final TokenType tokenType,
        final String tokenValue,
        final long tokenLifeTime
    ) {
        CookieUtil.addCookie(
            response,
            tokenType.name(),
            tokenValue,
            tokenLifeTime,
            cookieProperties.getDomain(),
            cookieProperties.getSameSite()
        );
    }
}
