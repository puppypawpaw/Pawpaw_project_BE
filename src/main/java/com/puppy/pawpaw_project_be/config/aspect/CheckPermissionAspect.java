package com.puppy.pawpaw_project_be.config.aspect;

import com.puppy.pawpaw_project_be.application.user.query.UserQuery;
import com.puppy.pawpaw_project_be.config.annotation.CheckPermission;
import com.puppy.pawpaw_project_be.domain.user.domain.Role;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.exception.common.PermissionRequiredException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckPermissionAspect {
    private final UserQuery userQuery;

    @Around("@annotation(checkPermission)")
    public Object validateRole(
        final ProceedingJoinPoint joinPoint,
        final CheckPermission checkPermission
    ) throws Throwable {
        Role requiredRole = checkPermission.role();
        UserId userId = null;

        for (Object arg : joinPoint.getArgs()) {
            if (arg.getClass().equals(UserId.class)) {
                userId = (UserId) arg;
                break;
            }
        }

        if (Objects.isNull(userId) || !userQuery.checkUserRole(userId, requiredRole)) {
            throw new PermissionRequiredException();
        }

        return joinPoint.proceed();
    }
}
