package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.ChatroomScheduleResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomScheduleResponse;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.api.util.user.UserUtil;
import kr.co.pawpaw.common.exception.chatroom.NotAChatroomScheduleParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotFoundChatroomScheduleException;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomScheduleParticipantResponse;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomScheduleCommand;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomScheduleParticipantCommand;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomScheduleParticipantQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomScheduleQuery;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.stream.Collectors;

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
    private UserService userService;
    @Mock
    private UserQuery userQuery;
    @Mock
    private ChatroomQuery chatroomQuery;
    @InjectMocks
    private ChatroomScheduleService chatroomScheduleService;

    User user = User.builder().build();

    @Test
    @DisplayName("createChatroomSchedule 메서드는 chatroomScheduleCommand로 저장한 엔티티의 id를 필드로 가지는 createChatroomScheduleResponse를 반환한다.")
    void createChatroomSchedule() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        //given
        Long chatroomId = 123L;
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
    @DisplayName("participateChatroomSchedule 메서드는 존재하는 채팅방 스케줄이 아닌 경우에 예외를 발생시킨다.")
    void participateChatroomScheduleNotFoundChatroomScheduleException() {
        //given
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> chatroomScheduleService.participateChatroomSchedule(user.getUserId(), chatroomId, chatroomScheduleId)).isInstanceOf(NotFoundChatroomScheduleException.class);

        //then
        verify(chatroomScheduleQuery).existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId);
    }

    @Test
    @DisplayName("participateChatroomSchedule 메서드는 chatroomScheduleParticipantCommand 의 save메서드를 호출한다.")
    void participateChatroomSchedule() {
        //given
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(true);

        //when
        chatroomScheduleService.participateChatroomSchedule(user.getUserId(), chatroomId, chatroomScheduleId);

        //then
        verify(chatroomScheduleQuery).existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId);
        verify(chatroomScheduleParticipantCommand).save(any(ChatroomScheduleParticipant.class));
    }

    @Nested
    @DisplayName("getNotEndChatroomScheduleList")
    class GetNotEndChatroomScheduleList {
        Long chatroomId = 123L;
        File defaultUserImage = File.builder()
            .fileName(UserUtil.getUserDefaultImageName())
            .fileUrl("기본 url")
            .build();

        List<ChatroomScheduleData> responseDataList = List.of(new ChatroomScheduleData(
            1L,
            "name",
            "schedule",
            LocalDateTime.now(),
            LocalDateTime.now(),
            List.of(
                new ChatroomScheduleParticipantResponse(
                    "null image",
                    null
                ),
                new ChatroomScheduleParticipantResponse(
                    "not null image",
                    "imageUrl"
                )
            )
        ));

        Constructor<ChatroomScheduleResponse> constructor = ChatroomScheduleResponse.class.getDeclaredConstructor(
            Long.class,
            String.class,
            String.class,
            String.class,
            String.class,
            Collection.class
        );

        GetNotEndChatroomScheduleList() throws NoSuchMethodException {
        }

        @BeforeEach
        void setup() {
            constructor.setAccessible(true);
        }

        @Test
        @DisplayName("null인 채팅방 스케줄 참가자 이미지 url을 기본 이미지 url로 변경하고 LocalDateTime을 string으로 변환한다.")
        void changeNullParticipantImageUrlToDefaultImageUrl() {
            //given
            when(userService.getUserDefaultImageUrl()).thenReturn(defaultUserImage.getFileUrl());
            when(chatroomScheduleQuery.findNotEndChatroomScheduleByChatroomId(chatroomId)).thenReturn(responseDataList);

            List<ChatroomScheduleResponse> resultExpected = responseDataList.stream()
                .map(data -> {
                    data.getParticipants().forEach(
                        participantResponse -> participantResponse.updateImageUrl(defaultUserImage.getFileUrl())
                    );

                    try {
                        return constructor.newInstance(
                            data.getId(),
                            data.getName(),
                            data.getDescription(),
                            TimeUtil.localDateTimeToDefaultTimeString(data.getStartDate()),
                            TimeUtil.localDateTimeToDefaultTimeString(data.getEndDate()),
                            data.getParticipants()
                        );
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

            //when
            List<ChatroomScheduleResponse> result = chatroomScheduleService.getNotEndChatroomScheduleList(user.getUserId(), chatroomId);

            //then
            assertThat(result).usingRecursiveComparison()
                .isEqualTo(resultExpected);
        }
    }

    @Nested
    @DisplayName("leaveChatroomSchedule 메서드는")
    class LeaveChatroomSchedule {
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        UserId userId = UserId.create();
        ChatroomScheduleParticipant chatroomScheduleParticipant = ChatroomScheduleParticipant.builder().build();

        @Test
        @DisplayName("채팅방 스케줄이 존재하지 않으면 에외를 발생시킨다.")
        void NotFoundChatroomScheduleException() {
            //given
            when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(false);

            //when
            assertThatThrownBy(() -> chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, chatroomScheduleId))
                .isInstanceOf(NotFoundChatroomScheduleException.class);

            //then
        }
        @Test
        @DisplayName("채팅방 스케줄 참가자가 아니면 예외를 발생시킨다.")
        void NotAChatroomScheduleParticipantException() {
            //given
            when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(true);
            when(chatroomScheduleParticipantQuery.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId)).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, chatroomScheduleId))
                .isInstanceOf(NotAChatroomScheduleParticipantException.class);

            //then
        }
        @Test
        @DisplayName("chatroomScheduleParticipantCommand의 delete 메서드를 호출한다.")
        void callDeleteMethod() {
            //given
            when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(true);
            when(chatroomScheduleParticipantQuery.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId)).thenReturn(Optional.of(chatroomScheduleParticipant));

            //when
            chatroomScheduleService.leaveChatroomSchedule(userId, chatroomId, chatroomScheduleId);

            //then
            verify(chatroomScheduleParticipantCommand).delete(chatroomScheduleParticipant);
        }
    }

    @Nested
    @DisplayName("deleteChatroomSchedule 메서드는")
    class DeleteChatroomSchedule {
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        ChatroomSchedule chatroomSchedule = ChatroomSchedule.builder()
            .build();
        @Test
        @DisplayName("존재하는 채팅방 스케줄이 아니면 예외가 발생한다.")
        void notFoundChatroomScheduleException() {
            //given
            when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(false);

            //when
            assertThatThrownBy(() -> chatroomScheduleService.deleteChatroomSchedule(chatroomId, chatroomScheduleId))
                .isInstanceOf(NotFoundChatroomScheduleException.class);

            //then
        }

        @Test
        @DisplayName("chatroomScheduleCommand 의 deleteById 메서드를 호출한다.")
        void deleteById() {
            //given
            when(chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId)).thenReturn(true);

            //when
            chatroomScheduleService.deleteChatroomSchedule(chatroomId, chatroomScheduleId);

            //then
            verify(chatroomScheduleCommand).deleteById(chatroomId);
        }
    }
}