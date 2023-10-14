package kr.co.pawpaw.api.config.auth.handler;

import kr.co.pawpaw.api.config.auth.repository.CookieAuthorizationRequestRepository;
import kr.co.pawpaw.api.config.property.OAuth2Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final OAuth2Properties oAuth2Properties;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authenticationException
    ) throws IOException {
        String targetUrl = UriComponentsBuilder.fromUriString(oAuth2Properties.getRedirectUriFailure())
            .queryParam("error", authenticationException.getLocalizedMessage())
            .build().toUriString();

        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
