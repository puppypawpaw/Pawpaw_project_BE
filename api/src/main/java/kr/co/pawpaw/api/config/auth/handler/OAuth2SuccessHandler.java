package kr.co.pawpaw.api.config.auth.handler;

import kr.co.pawpaw.api.config.auth.object.OAuth2Attributes;
import kr.co.pawpaw.api.config.auth.provider.JwtTokenProvider;
import kr.co.pawpaw.api.config.auth.repository.CookieAuthorizationRequestRepository;
import kr.co.pawpaw.api.config.property.CookieProperties;
import kr.co.pawpaw.api.config.property.JwtProperties;
import kr.co.pawpaw.api.config.property.OAuth2Properties;
import kr.co.pawpaw.common.util.CookieUtil;
import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import kr.co.pawpaw.domainredis.auth.domain.RefreshToken;
import kr.co.pawpaw.domainredis.auth.domain.TokenType;
import kr.co.pawpaw.domainredis.auth.service.command.OAuth2TempAttributesCommand;
import kr.co.pawpaw.domainredis.auth.service.command.RefreshTokenCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2Properties oAuth2Properties;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenCommand refreshTokenCommand;
    private final CookieProperties cookieProperties;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final OAuth2TempAttributesCommand oAuth2TempAttributesCommand;

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException {
        String targetUrl = oAuth2Properties.getRedirectUriSuccess();

        OAuth2Attributes attributes = (OAuth2Attributes) (((OAuth2User) authentication.getPrincipal())
            .getAttributes()
            .get("oAuth2Attributes"));

        if (Objects.nonNull(attributes)) {
            String key = UUID.randomUUID().toString();
            OAuth2TempAttributes tempAttributes = oAuth2TempAttributesCommand.save(
                OAuth2TempAttributes.builder()
                    .key(key)
                    .email(attributes.getEmail())
                    .name(attributes.getName())
                    .provider(attributes.getProvider().name())
                    .profileImageUrl(attributes.getProfileImageUrl())
                    .build());
            targetUrl = oAuth2Properties.getRedirectUriSignUp() + tempAttributes.getKey();
        } else {
            String accessTokenValue = jwtTokenProvider.createAccessToken(authentication);
            String refreshTokenValue = jwtTokenProvider.createRefreshToken();

            RefreshToken refreshToken = RefreshToken.builder()
                .userId(authentication.getName())
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

        if (response.isCommitted()) {
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(
            request,
            response,
            targetUrl
        );
    }

    protected void clearAuthenticationAttributes(
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
