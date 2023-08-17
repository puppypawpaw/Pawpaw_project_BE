package kr.co.pawpaw.api.config.resolver;

import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.common.exception.user.NotSignedInException;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
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
            throw new NotSignedInException();
        }

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

        return UserId.of(principal.getUsername());
    }
}
