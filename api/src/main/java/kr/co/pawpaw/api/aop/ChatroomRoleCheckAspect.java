package kr.co.pawpaw.api.aop;

import kr.co.pawpaw.common.exception.chatroom.NotAChatroomParticipantException;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class ChatroomRoleCheckAspect {
    private final ChatroomParticipantQuery chatroomParticipantQuery;

    @Around("@annotation(chatroomRoleCheck)")
    public Object chatroomRoleCheck(
        final ProceedingJoinPoint joinPoint,
        final ChatroomRoleCheck chatroomRoleCheck
    ) throws Throwable {
        ChatroomParticipantRole role = chatroomRoleCheck.role();
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();;
        Method method = signature.getMethod();
        UserId userId = null;
        Long chatroomId = null;
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            String parameterName = parameters[i].getName();
            if (parameterName.equals("chatroomId")) {
                chatroomId = (Long) args[i];
            } else if (parameterName.equals("userId")) {
                userId = (UserId) args[i];
            }
        }

        if (Objects.isNull(userId) || Objects.isNull(chatroomId)) {
            throw new IllegalArgumentException();
        }

        Boolean isManager = chatroomParticipantQuery.findByUserIdAndChatroomId(userId, chatroomId)
            .map(ChatroomParticipant::isManager)
            .orElseThrow(NotAChatroomParticipantException::new);

        if (!isManager && role.equals(ChatroomParticipantRole.MANAGER)) {
            throw new PermissionRequiredException();
        }

        return joinPoint.proceed();
    }
}
