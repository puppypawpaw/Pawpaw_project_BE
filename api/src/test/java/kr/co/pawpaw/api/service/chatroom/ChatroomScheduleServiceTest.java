package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.ChatroomScheduleResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleResponse;
import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.common.exception.chatroom.NotAChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotAChatroomScheduleParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotFoundChatroomScheduleException;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomScheduleCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomScheduleParticipantCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomScheduleParticipantQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomScheduleQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomScheduleServiceTest {
    @Mock
    private ChatroomScheduleQuery chatroomScheduleQuery;
    @Mock
    private ChatroomScheduleCommand chatroomScheduleCommand;
    @Mock
    private ChatroomScheduleParticipantQuery chatroomScheduleParticipantQuery;
    @Mock
    private ChatroomScheduleParticipantCommand chatroomScheduleParticipantCommand;
    @Mock
    private ChatroomParticipantQuery chatroomParticipantQuery;
    @Mock
    private UserQuery userQuery;
    @Mock
    private ChatroomQuery chatroomQuery;
    @InjectMocks
    private ChatroomScheduleService chatroomScheduleService;

    User user = User.builder().build();

    @Test
    @DisplayName("createChatroomSchedule 메서드는 채팅방 참여자가 아니면 예외를 발생시킨다.")
    void createChatroomScheduleNotAChatroomParticipantException() {
        //given
        Long chatroomId = 123L;
        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(false);
        CreateChatroomScheduleRequest request = CreateChatroomScheduleRequest.builder()
            .build();

        //when
        assertThatThrownBy(() -> chatroomScheduleService.createChatroomSchedule(user.getUserId(), chatroomId, request)).isInstanceOf(NotAChatroomParticipantException.class);

        //then
        verify(chatroomParticipantQuery).existsByUserIdAndChatroomId(user.getUserId(), chatroomId);
    }

    @Test
    @DisplayName("createChatroomSchedule 메서드는 chatroomScheduleCommand로 저장한 엔티티의 id를 필드로 가지는 createChatroomScheduleResponse를 반환한다.")
    void createChatroomSchedule() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        //given
        Long chatroomId = 123L;
        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(true);
        ChatroomSchedule chatroomSchedule = ChatroomSchedule.builder()
            .build();

        Field idField = ChatroomSchedule.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(chatroomSchedule, chatroomId);

        when(chatroomScheduleCommand.save(any(ChatroomSchedule.class))).thenReturn(chatroomSchedule);
        CreateChatroomScheduleRequest request = CreateChatroomScheduleRequest.builder()
            .name("chatroomschedule-name")
            .description("chatroomschedule-description")
            .startDate("2023-09-16 00:23:00")
            .endDate("2023-09-16 00:24:00")
            .build();

        Constructor<CreateChatroomScheduleResponse> constructor = CreateChatroomScheduleResponse.class.getDeclaredConstructor(Long.class);
        constructor.setAccessible(true);

        CreateChatroomScheduleResponse resultExpected = constructor.newInstance(123L);

        //when
        CreateChatroomScheduleResponse response = chatroomScheduleService.createChatroomSchedule(user.getUserId(), chatroomId, request);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(resultExpected);
    }

    @Test
    @DisplayName("participateChatroomSchedule 메서드는 채팅방 참여자가 아니면 예외를 발생시킨다.")
    void participateChatroomScheduleNotAChatroomParticipantException() {
        //given
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> chatroomScheduleService.participateChatroomSchedule(user.getUserId(), chatroomId, chatroomScheduleId)).isInstanceOf(NotAChatroomParticipantException.class);

        //then
        verify(chatroomParticipantQuery).existsByUserIdAndChatroomId(user.getUserId(), chatroomId);
    }

    @Test
    @DisplayName("participateChatroomSchedule 메서드는 존재하는 채팅방 스케줄이 아닌 경우에 예외를 발생시킨다.")
    void participateChatroomScheduleNotFoundChatroomScheduleException() {
        //given
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(true);
        when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> chatroomScheduleService.participateChatroomSchedule(user.getUserId(), chatroomId, chatroomScheduleId)).isInstanceOf(NotFoundChatroomScheduleException.class);

        //then
        verify(chatroomParticipantQuery).existsByUserIdAndChatroomId(user.getUserId(), chatroomId);
        verify(chatroomScheduleQuery).existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId);
    }

    @Test
    @DisplayName("participateChatroomSchedule 메서드는 chatroomScheduleParticipantCommand 의 save메서드를 호출한다.")
    void participateChatroomSchedule() {
        //given
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(true);
        when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(true);

        //when
        chatroomScheduleService.participateChatroomSchedule(user.getUserId(), chatroomId, chatroomScheduleId);

        //then
        verify(chatroomParticipantQuery).existsByUserIdAndChatroomId(user.getUserId(), chatroomId);
        verify(chatroomScheduleQuery).existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId);
        verify(chatroomScheduleParticipantCommand).save(any(ChatroomScheduleParticipant.class));
    }

    @Test
    @DisplayName("getNotEndChatroomScheduleList 메서드는 채팅방 참여자가 아니면 예외를 발생시킨다.")
    void getNotEndChatroomScheduleListNotAChatroomParticipantException() {
        //given
        Long chatroomId = 123L;
        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> chatroomScheduleService.getNotEndChatroomScheduleList(user.getUserId(), chatroomId)).isInstanceOf(NotAChatroomParticipantException.class);

        //then
        verify(chatroomParticipantQuery).existsByUserIdAndChatroomId(user.getUserId(), chatroomId);
    }

    @Test
    @DisplayName("getNotEndChatroomScheduleList 메서드는 chatroomScheduleQuery의 findNotEndChatroomScheduleByChatroomId 를 호출하고 ChatroomScheduleData의 LocalDateTime을 string으로 변환한다.")
    void getNotEndChatroomScheduleList() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //given
        Long chatroomId = 123L;
        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(true);
        ChatroomScheduleData responseData = new ChatroomScheduleData(
            1L,
            "name",
            "schedule",
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of()
        );

        List<ChatroomScheduleData> responseDataList = List.of(responseData);

        Constructor<ChatroomScheduleResponse> constructor = ChatroomScheduleResponse.class.getDeclaredConstructor(
            Long.class,
            String.class,
            String.class,
            String.class,
            String.class,
            Collection.class
        );

        constructor.setAccessible(true);

        List<ChatroomScheduleResponse> resultExpected = List.of(
            constructor.newInstance(
                responseData.getId(),
                responseData.getName(),
                responseData.getDescription(),
                TimeUtil.localDateTimeToDefaultTimeString(responseData.getStartDate()),
                TimeUtil.localDateTimeToDefaultTimeString(responseData.getEndDate()),
                List.of()
            )
        );

        when(chatroomScheduleQuery.findNotEndChatroomScheduleByChatroomId(chatroomId)).thenReturn(responseDataList);

        //when
        List<ChatroomScheduleResponse> result = chatroomScheduleService.getNotEndChatroomScheduleList(user.getUserId(), chatroomId);

        //then
        verify(chatroomParticipantQuery).existsByUserIdAndChatroomId(user.getUserId(), chatroomId);
        verify(chatroomScheduleQuery).findNotEndChatroomScheduleByChatroomId(chatroomId);
        assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
    }

    @Nested
    @DisplayName("채팅방 스케줄 나가기 메서드는")
    class LeaveChatroomSchedule {
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        UserId userId = UserId.create();
        ChatroomScheduleParticipant chatroomScheduleParticipant = ChatroomScheduleParticipant.builder().build();

        @Nested
        @DisplayName("채팅방 참가자가")
        class ChatroomParticipant {
            @Test
            @DisplayName("아니면 예외를 발생시킨다.")
            void NotAChatroomParticipantException() {
                //given

                when(chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, chatroomId)).thenReturn(false);

                //when
                assertThatThrownBy(() -> chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, chatroomScheduleId))
                    .isInstanceOf(NotAChatroomParticipantException.class);

                //then
            }
            @Nested
            @DisplayName("맞고 채팅방 스케줄이")
            class ChatroomSchedule {
                @Test
                @DisplayName("존재하지 않으면 에외를 발생시킨다.")
                void NotFoundChatroomScheduleException() {
                    //given
                    when(chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, chatroomId)).thenReturn(true);
                    when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(false);

                    //when
                    assertThatThrownBy(() -> chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, chatroomScheduleId))
                        .isInstanceOf(NotFoundChatroomScheduleException.class);

                    //then
                }
                @Nested
                @DisplayName("존재하고 채팅방 스케줄 참가자가")
                class ChatroomScheduleParticipant {
                    @Test
                    @DisplayName("아니면 예외를 발생시킨다.")
                    void NotAChatroomScheduleParticipantException() {
                        //given
                        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, chatroomId)).thenReturn(true);
                        when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(true);
                        when(chatroomScheduleParticipantQuery.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId)).thenReturn(Optional.empty());

                        //when
                        assertThatThrownBy(() -> chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, chatroomScheduleId))
                            .isInstanceOf(NotAChatroomScheduleParticipantException.class);

                        //then
                    }
                    @Test
                    @DisplayName("맞으면 chatroomScheduleParticipantCommand의 delete 메서드를 호출한다.")
                    void callDeleteMethod() {
                        //given
                        when(chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, chatroomId)).thenReturn(true);
                        when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(true);
                        when(chatroomScheduleParticipantQuery.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId)).thenReturn(Optional.of(chatroomScheduleParticipant));

                        //when
                        chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, chatroomScheduleId);

                        //then
                        verify(chatroomScheduleParticipantCommand).delete(chatroomScheduleParticipant);
                    }
                }
            }
        }
    }
}