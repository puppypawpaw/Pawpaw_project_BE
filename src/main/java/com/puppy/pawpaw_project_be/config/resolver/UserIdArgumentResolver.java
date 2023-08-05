package com.puppy.pawpaw_project_be.config.resolver;

import com.puppy.pawpaw_project_be.config.annotation.AuthenticatedUserId;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedUserId.class);
    }

    @Override
    public UserId resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return UserId.of(null);
        }

        UserDetails principal = (UserDetails)SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        return UserId.of(principal.getUsername());
    }
}
