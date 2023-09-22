package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.service.chatroom.ChatroomService;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.dto.chatroom.ChatroomDetailResponse;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomRequest;
import kr.co.pawpaw.api.dto.chatroom.CreateChatroomResponse;
import kr.co.pawpaw.common.exception.chatroom.IsNotChatroomParticipantException;
import kr.co.pawpaw.common.exception.chatroom.NotAllowedChatroomLeaveException;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomDetailData;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomResponse;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.command.ChatroomParticipantCommand;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.domainrdb.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomServiceTest {
    @Mock
    private ChatroomCommand chatroomCommand;
    @Mock
    private ChatroomParticipantCommand chatroomParticipantCommand;
    @Mock
    private ChatroomParticipantQuery chatroomParticipantQuery;
    @Mock
    private ChatroomQuery chatroomQuery;
    @Mock
    private UserQuery userQuery;
    @Mock
    private FileService fileService;
    @Mock
    private MultipartFile multipartFile;
    @InjectMocks
    private ChatroomService chatroomService;

    private final User user = User.builder().build();
    private final CreateChatroomRequest request = CreateChatroomRequest.builder()
        .name("chatroom-name")
        .description("chatroom-description")
        .locationLimit(true)
        .searchable(false)
        .hashTagList(List.of("hashTag1", "hashTag2"))
        .build();

    private final File file = File.builder()
        .fileName(UUID.randomUUID().toString())
        .uploader(user)
        .byteSize(123L)
        .contentType("contentType")
        .build();


    @Test
    @DisplayName("createChatroom메서드는 존재하지 않는 유저 예외가 발생가능하다.")
    void createChatroomNotFoundUserException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> chatroomService.createChatroom(user.getUserId(), request, multipartFile)).isInstanceOf(NotFoundUserException.class);

        //then
        verify(userQuery).findByUserId(user.getUserId());
    }

    @Test
    @DisplayName("createChatroom메서드는 생성된 chatroom의 multipartFile이 null이거나 길이가 0이면 fileService의 saveFileByMultipartFile메서드를 호출하지 않는다.")
    void createChatroomNoMultipartFile() throws IOException {
        //given
        Chatroom chatroom = request.toChatroom(file);
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(chatroomCommand.save(any(Chatroom.class))).thenReturn(chatroom);
        when(multipartFile.getBytes()).thenReturn(new byte[0]);

        //when
        chatroomService.createChatroom(user.getUserId(), request, null);
        chatroomService.createChatroom(user.getUserId(), request, multipartFile);

        //then
        verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
    }

    @Test
    @DisplayName("createChatroom메서드는 생성된 chatroom의 Id를 필드로 가지는 CreateChatroomResponse를 반환한다.")
    void createChatroom() throws IOException, NoSuchFieldException, IllegalAccessException {
        //given
        Chatroom chatroom = request.toChatroom(file);
        Field idField = chatroom.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        Long id = 1234L;
        idField.set(chatroom, id);
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        when(chatroomCommand.save(any(Chatroom.class))).thenReturn(chatroom);
        when(multipartFile.getBytes()).thenReturn(new byte[file.getByteSize().intValue()]);
        when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(file);
        //when
        CreateChatroomResponse response = chatroomService.createChatroom(user.getUserId(), request, multipartFile);

        //then
        assertThat(response.getChatroomId()).isEqualTo(id);
    }

    @Test
    @DisplayName("joinChatroom 메서드는 존재하지 않는 유저 예외가 발생 가능하다.")
    void joinChatroomNotFoundUserException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());
        Long chatroomId = 12345L;
        //when
        assertThatThrownBy(() -> chatroomService.joinChatroom(user.getUserId(), chatroomId)).isInstanceOf(NotFoundUserException.class);

        //then
    }

    @Test
    @DisplayName("joinChatroom 메서드는 participant 타입으로 참여자를 저장한다.")
    void joinChatroom() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        Long chatroomId = 12345L;
        //when
        chatroomService.joinChatroom(user.getUserId(), chatroomId);

        //then
        ArgumentCaptor<ChatroomParticipant> chatroomParticipantArgumentCaptor = ArgumentCaptor.forClass(ChatroomParticipant.class);
        verify(chatroomParticipantCommand).save(chatroomParticipantArgumentCaptor.capture());
        assertThat(chatroomParticipantArgumentCaptor.getValue().isManager()).isFalse();
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 존재하지 않는 유저 예외가 발생 가능하다.")
    void leaveChatroomNotFoundUserException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());
        Long chatroomId = 12345L;
        //when
        assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroomId)).isInstanceOf(NotFoundUserException.class);

        //then
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 채팅방 참여자가 아니라는 예외가 발생할 수 있다.")
    void leaveChatroomIsNotChatroomParticipantException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        Long chatroomId = 12345L;
        when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroomId)).isInstanceOf(IsNotChatroomParticipantException.class);

        //then
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 매니저는 호출 불가능하다.")
    void leaveChatroomNotAllowedChatroomLeaveException() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        Long chatroomId = 12345L;
        when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(Optional.of(ChatroomParticipant.builder()
                .role(ChatroomParticipantRole.MANAGER)
            .build()));

        //when
        assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroomId)).isInstanceOf(NotAllowedChatroomLeaveException.class);

        //then
    }

    @Test
    @DisplayName("leaveChatroom 메서드는 chatroomParticipantCommand의 delete메서드를 호출한다.")
    void leaveChatroom() {
        //given
        when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
        Long chatroomId = 12345L;
        ChatroomParticipant chatroomParticipant = ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.PARTICIPANT)
            .build();
        when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroomId)).thenReturn(Optional.of(chatroomParticipant));

        //when
        chatroomService.leaveChatroom(user.getUserId(), chatroomId);

        //then
        verify(chatroomParticipantCommand).delete(chatroomParticipant);
    }

    @Test
    @DisplayName("getParticipatedChatroomList 메서드는 chatroomQuery의 getParticipatedChatroomDetailDataByUserId 메서드를 호출한다.")
    void getParticipatedChatroomList() {
        //given
        UserId userId = UserId.create();

        List<ChatroomDetailData> chatroomDetailData = List.of(
            new ChatroomDetailData(
                1L,
                "name",
                "description",
                "coverUrl",
                LocalDateTime.now().minusDays(1),
                List.of("hashTag1", "hashTag2"),
                "managerName",
                "managerImageUrl",
                2L,
                false,
                false
            )
        );

        List<ChatroomDetailResponse> resultExpected = chatroomDetailData.stream()
            .map(ChatroomDetailResponse::of)
            .collect(Collectors.toList());

        when(chatroomQuery.getParticipatedChatroomDetailDataByUserId(userId)).thenReturn(chatroomDetailData);

        //when
        List<ChatroomDetailResponse> result = chatroomService.getParticipatedChatroomList(userId);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        verify(chatroomQuery).getParticipatedChatroomDetailDataByUserId(userId);
    }

    @Test
    @DisplayName("getRecommendedNewChatroomList 메서드는 chatroomQuery의 getAccessibleNewChatroomByUserId 메서드를 호출함")
    void getRecommendedChatroomList() {
        //given
        UserId userId = UserId.create();

        when(chatroomQuery.getAccessibleNewChatroomByUserId(userId)).thenReturn(List.of(new ChatroomResponse(
            1L,
            "name",
            "description",
            List.of("hashTag1"),
            "managerName",
            "managerImageUrl",
            2L
        )));

        //when
        chatroomService.getRecommendedNewChatroomList(userId);

        //then
        verify(chatroomQuery).getAccessibleNewChatroomByUserId(userId);
    }

    @Test
    @DisplayName("getTrandingChatroomList 메서드는 chatroomQuery의 getAccessibleTrandingChatroom를 호출한다.")
    void getTrandingChatroomList() {
        //given
        UserId userId = UserId.create();
        Long beforeId = null;
        int size = 10;

        when(chatroomQuery.getAccessibleTrandingChatroom(userId, beforeId, size)).thenReturn(new SliceImpl<>(
            List.of(), PageRequest.of(0, 10),  false
        ));

        //when
        chatroomService.getTrandingChatroomList(userId, beforeId, size);

        //then
        verify(chatroomQuery).getAccessibleTrandingChatroom(userId, beforeId, size);
    }
}