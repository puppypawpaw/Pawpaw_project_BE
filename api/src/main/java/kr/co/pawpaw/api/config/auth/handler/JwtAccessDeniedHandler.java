package kr.co.pawpaw.api.config.auth.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AccessDeniedException accessDeniedException
    ) {
        throw new AccessDeniedException("");
    }
}
