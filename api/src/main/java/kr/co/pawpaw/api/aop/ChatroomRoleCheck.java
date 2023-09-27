package kr.co.pawpaw.api.aop;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChatroomRoleCheck {
    ChatroomParticipantRole role() default ChatroomParticipantRole.MANAGER;
}
