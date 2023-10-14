package kr.co.pawpaw.api.config.auth.repository;


import kr.co.pawpaw.api.config.property.CookieProperties;
import kr.co.pawpaw.api.config.property.OAuth2Properties;
import kr.co.pawpaw.api.util.cookie.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository {
    private final CookieProperties cookieProperties;
    private final OAuth2Properties oAuth2Properties;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(final HttpServletRequest request) {
        return CookieUtil.getCookie(request, oAuth2Properties.getCookieName())
            .map(cookie -> CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class))
            .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(
        final OAuth2AuthorizationRequest authorizationRequest,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        if (authorizationRequest == null) {
            CookieUtil.deleteCookie(request, response, oAuth2Properties.getCookieName());
            return;
        }

        CookieUtil.addCookie(
            response,
            oAuth2Properties.getCookieName(),
            authorizationRequest,
            oAuth2Properties.getCookieExpireSeconds(),
            cookieProperties.getDomain(),
            cookieProperties.getSameSite()
        );
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(final HttpServletRequest request) {
        return loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        CookieUtil.deleteCookie(request, response, oAuth2Properties.getCookieName());
    }
}
