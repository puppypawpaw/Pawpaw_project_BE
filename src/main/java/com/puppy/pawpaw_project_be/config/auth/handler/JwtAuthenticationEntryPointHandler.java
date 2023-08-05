package com.puppy.pawpaw_project_be.config.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.puppy.pawpaw_project_be.exception.common.ErrorCode;
import com.puppy.pawpaw_project_be.exception.common.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authException
    ) throws IOException {
        if (authException.getClass().equals(BadCredentialsException.class)) {
            throw new BadCredentialsException("");
        }

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.HANDLE_AUTHENTICATION_ENTRYPOINT);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        try (OutputStream outputStream = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, errorResponse);
            outputStream.flush();
        }
    }
}
