package com.puppy.pawpaw_project_be.config.auth.handler;

import com.puppy.pawpaw_project_be.application.auth.command.SignService;
import com.puppy.pawpaw_project_be.config.auth.repository.CookieAuthorizationRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final String redirectUriSuccess;
    private final SignService signService;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    public OAuth2SuccessHandler(
        @Value("${spring.security.oauth2.redirectUriSuccess}") final String redirectUriSuccess,
        final SignService signService,
        final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository
    ) {
        this.redirectUriSuccess = redirectUriSuccess;
        this.signService = signService;
        this.cookieAuthorizationRequestRepository = cookieAuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(
            request,
            response,
            UriComponentsBuilder.fromUriString(targetUrl)
                .build()
                .toUriString());
    }

    protected String determineTargetUrl(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) {
        signService.signIn(response, authentication);

        return UriComponentsBuilder.fromUriString(redirectUriSuccess)
            .build()
            .toUriString();
    }

    protected void clearAuthenticationAttributes(
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
