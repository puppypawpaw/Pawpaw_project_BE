package kr.co.pawpaw.api.config.aspect;

import kr.co.pawpaw.api.config.annotation.CheckPermission;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
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

        if (Objects.isNull(userId) || !userQuery.existsByUserIdAndRole(userId, requiredRole)) {
            throw new PermissionRequiredException();
        }

        return joinPoint.proceed();
    }
}
