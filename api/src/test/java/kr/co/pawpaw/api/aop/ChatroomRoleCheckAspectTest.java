package kr.co.pawpaw.api.aop;

import kr.co.pawpaw.common.exception.chatroom.NotAChatroomParticipantException;
import kr.co.pawpaw.common.exception.common.PermissionRequiredException;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Nested
@DisplayName("채팅방 권한 체크는")
class ChatroomRoleCheckAspectTest {
    @Mock
    private ChatroomParticipantQuery chatroomParticipantQuery;
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;
    @Mock
    private MethodSignature methodSignature;
    @Mock
    private ChatroomRoleCheck chatroomRoleCheck;
    @InjectMocks
    private ChatroomRoleCheckAspect chatroomRoleCheckAspect;

    private static final UserId userId = UserId.create();
    private static final Long chatroomId = 123L;
    private static final ChatroomParticipant isNotManager = ChatroomParticipant.builder()
        .role(ChatroomParticipantRole.PARTICIPANT)
        .build();
    private static final ChatroomParticipant isManager = ChatroomParticipant.builder()
        .role(ChatroomParticipantRole.MANAGER)
        .build();

    private void aopTestMethodUserIdAndLongIsNotParameter(
    ) {

    }

    private void aopTestMethodLongIsNotParameter(
        final UserId userId
    ) {

    }

    private void aopTestMethod(
        final UserId userId,
        final Long chatroomId
    ) {

    }

    @Nested
    @DisplayName("userId 파라미터가")
    class UserIdParameter {
        @Test
        @DisplayName("존재하지 않으면 예외가 발생한다.")
        void notExistsIllegalArgumentException() throws NoSuchMethodException {
            //given
            Method method = ChatroomRoleCheckAspectTest.class.getDeclaredMethod("aopTestMethodUserIdAndLongIsNotParameter");
            when(chatroomRoleCheck.role()).thenReturn(ChatroomParticipantRole.MANAGER);
            when(proceedingJoinPoint.getArgs()).thenReturn(new Object[] {});
            when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
            when(methodSignature.getMethod()).thenReturn(method);

            //when
            assertThatThrownBy(() -> chatroomRoleCheckAspect.chatroomRoleCheck(proceedingJoinPoint, chatroomRoleCheck))
                .isInstanceOf(IllegalArgumentException.class);

            //then
        }

        @Nested
        @DisplayName("존재하고 long parameter가")
        class LongParameter {
            @Test
            @DisplayName("존재하지 않으면 예외가 발생한다.")
            void notExistsIllegalArgumentException() throws NoSuchMethodException {
                //given
                Method method = ChatroomRoleCheckAspectTest.class.getDeclaredMethod("aopTestMethodLongIsNotParameter", UserId.class);
                when(chatroomRoleCheck.role()).thenReturn(ChatroomParticipantRole.MANAGER);
                when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{userId});
                when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
                when(methodSignature.getMethod()).thenReturn(method);

                //when
                assertThatThrownBy(() -> chatroomRoleCheckAspect.chatroomRoleCheck(proceedingJoinPoint, chatroomRoleCheck))
                    .isInstanceOf(IllegalArgumentException.class);

                //then
            }

            @Nested
            @DisplayName("존재하고 채팅방 참석자가")
            class ChatroomParticipant {
                @Test
                @DisplayName("아니면 예외가 발생한다.")
                void NotAChatroomParticipantException() throws NoSuchMethodException {
                    //given
                    Method method = ChatroomRoleCheckAspectTest.class.getDeclaredMethod("aopTestMethod", UserId.class, Long.class);
                    when(chatroomRoleCheck.role()).thenReturn(ChatroomParticipantRole.MANAGER);
                    when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{userId, chatroomId});
                    when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
                    when(methodSignature.getMethod()).thenReturn(method);
                    when(chatroomParticipantQuery.findByUserIdAndChatroomId(userId, chatroomId)).thenReturn(Optional.empty());

                    //when
                    assertThatThrownBy(() -> chatroomRoleCheckAspect.chatroomRoleCheck(proceedingJoinPoint, chatroomRoleCheck))
                        .isInstanceOf(NotAChatroomParticipantException.class);

                    //then
                }

                @Nested
                @DisplayName("맞고 매니저가")
                class Manager {
                    @Test
                    @DisplayName("아니면 예외가 발생한다.")
                    void PermissionRequiredException() throws NoSuchMethodException {
                        //given
                        Method method = ChatroomRoleCheckAspectTest.class.getDeclaredMethod("aopTestMethod", UserId.class, Long.class);
                        when(chatroomRoleCheck.role()).thenReturn(ChatroomParticipantRole.MANAGER);
                        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{userId, chatroomId});
                        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
                        when(methodSignature.getMethod()).thenReturn(method);
                        when(chatroomParticipantQuery.findByUserIdAndChatroomId(userId, chatroomId)).thenReturn(Optional.of(isNotManager));

                        //when
                        assertThatThrownBy(() -> chatroomRoleCheckAspect.chatroomRoleCheck(proceedingJoinPoint, chatroomRoleCheck))
                            .isInstanceOf(PermissionRequiredException.class);

                        //then
                    }

                    @Test
                    @DisplayName("맞으면 예외가 발생하지 않는다.")
                    void NoException() throws NoSuchMethodException {
                        //given
                        Method method = ChatroomRoleCheckAspectTest.class.getDeclaredMethod("aopTestMethod", UserId.class, Long.class);
                        when(chatroomRoleCheck.role()).thenReturn(ChatroomParticipantRole.MANAGER);
                        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{userId, chatroomId});
                        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
                        when(methodSignature.getMethod()).thenReturn(method);
                        when(chatroomParticipantQuery.findByUserIdAndChatroomId(userId, chatroomId)).thenReturn(Optional.of(isManager));

                        //when
                        assertThatNoException().isThrownBy(() -> chatroomRoleCheckAspect.chatroomRoleCheck(proceedingJoinPoint, chatroomRoleCheck));

                        //then
                    }
                }
            }
        }

//        @Test
//        @DisplayName("존재하지 않으면 예외가 발생한다.")
//        void NullIllegalArgumentException() {
//            //given
//            when(chatroomRoleCheck.role()).thenReturn(ChatroomParticipantRole.MANAGER);
//            when(proceedingJoinPoint.getArgs()).thenReturn(objects);
//            when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
//            when(methodSignature.getMethod()).thenReturn(method);
//            when(method.getParameters()).thenReturn(new Parameter[]{parameter1});
//            when(parameter1.getName()).thenReturn("chatroomId");
//
//            //when
//            assertThatThrownBy(() -> chatroomRoleCheckAspect.chatroomRoleCheck(proceedingJoinPoint, chatroomRoleCheck))
//                .isInstanceOf(IllegalArgumentException.class);
//
//            //then
//        }
    }
}