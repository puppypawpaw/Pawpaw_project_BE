package kr.co.pawpaw.api.service.chatroom;

import kr.co.pawpaw.api.dto.chatroom.*;
import kr.co.pawpaw.api.service.file.FileService;
import kr.co.pawpaw.api.service.user.UserService;
import kr.co.pawpaw.common.exception.chatroom.*;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.dynamodb.chat.domain.Chat;
import kr.co.pawpaw.dynamodb.chat.domain.ChatType;
import kr.co.pawpaw.dynamodb.chat.dto.ChatMessageDto;
import kr.co.pawpaw.dynamodb.chat.service.command.ChatCommand;
import kr.co.pawpaw.dynamodb.chat.service.query.ChatQuery;
import kr.co.pawpaw.dynamodb.util.chat.ChatUtil;
import kr.co.pawpaw.mysql.chatroom.domain.*;
import kr.co.pawpaw.mysql.chatroom.dto.*;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomCommand;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomParticipantCommand;
import kr.co.pawpaw.mysql.chatroom.service.command.TrendingChatroomCommand;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomDefaultCoverQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomParticipantQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomQuery;
import kr.co.pawpaw.mysql.chatroom.service.query.TrendingChatroomQuery;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.dto.ChatMessageUserDto;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import kr.co.pawpaw.redis.service.pub.RedisPublisher;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private ChatroomDefaultCoverQuery chatroomDefaultCoverQuery;
    @Mock
    private TrendingChatroomCommand trendingChatroomCommand;
    @Mock
    private TrendingChatroomQuery trendingChatroomQuery;
    @Mock
    private ChatroomQuery chatroomQuery;
    @Mock
    private ChatCommand chatCommand;
    @Mock
    private UserQuery userQuery;
    @Mock
    private FileService fileService;
    @Mock
    private UserService userService;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    private ChannelTopic channelTopic;
    @Mock
    private ChatQuery chatQuery;
    @Mock
    private RedisPublisher redisPublisher;
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

    private final CreateChatroomWithDefaultCoverRequest requestWithDefaultCover = CreateChatroomWithDefaultCoverRequest.builder()
        .name("chatroom-name")
        .description("chatroom-description")
        .locationLimit(true)
        .searchable(false)
        .hashTagList(List.of("hashTag1", "hashTag2"))
        .coverId(12345L)
        .build();

    private final File file = File.builder()
        .fileName(UUID.randomUUID().toString())
        .uploader(user)
        .byteSize(123L)
        .contentType("contentType")
        .build();

    private final ChatroomDefaultCover cover = ChatroomDefaultCover.builder()
        .coverFile(file)
        .build();

    private static final Chatroom chatroom = Chatroom.builder()
        .build();

    Chat chat = Chat.builder()
        .chatroomId(123L)
        .chatType(ChatType.MESSAGE)
        .data("안녕하세요")
        .senderId("senderId")
        .build();

    @BeforeAll
    static void setup() throws NoSuchFieldException, IllegalAccessException {
        Field idField = Chatroom.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(chatroom, 123L);
    }

    @Nested
    @DisplayName("deleteChatroom 메서드는")
    class DeleteChatroom {
        Long chatroomId = 123L;
        ChatroomParticipant participant1 = ChatroomParticipant.builder().build();
        ChatroomParticipant participant2 = ChatroomParticipant.builder().build();
        List<ChatroomParticipant> twoParticipants = List.of(participant1, participant2);
        List<ChatroomParticipant> oneParticipants = List.of(participant1);

        @Test
        @DisplayName("채팅방 참가자가 1명 초과면(방장 외에 존재하면) 예외가 발생한다.")
        void chatroomParticipantExistException() {
            //given
            when(chatroomParticipantQuery.findAllByChatroomId(chatroomId)).thenReturn(twoParticipants);

            //when
            assertThatThrownBy(() -> chatroomService.deleteChatroom(chatroomId))
                .isInstanceOf(ChatroomParticipantExistException.class);

            //then

        }

        @Test
        @DisplayName("잔여 채팅방 참가자를 삭제, 뜨고있는 채팅방에서 채팅방을 삭제 후 채팅방을 삭제한다.")
        void deleteTrendingChatroom() {
            //given
            when(chatroomParticipantQuery.findAllByChatroomId(chatroomId)).thenReturn(oneParticipants);

            //when
            chatroomService.deleteChatroom(chatroomId);

            //then
            verify(chatroomParticipantCommand).delete(participant1);
            verify(trendingChatroomCommand).deleteByChatroomId(chatroomId);
            verify(chatroomCommand).deleteById(chatroomId);
        }
    }

    @Nested
    @DisplayName("updateChatroomManager 메서드는")
    class UpdateChatroomManager {
        Long chatroomId = 123L;
        User user1 = User.builder()
            .nickname("user1-nickname")
            .build();
        User user2 = User.builder()
            .nickname("user2-nickname")
            .build();

        ChatroomParticipant currentManager = ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.MANAGER)
            .user(user1)
            .build();

        ChatroomParticipant nextManager = ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.PARTICIPANT)
            .user(user2)
            .build();

        UpdateChatroomManagerRequest request1 = UpdateChatroomManagerRequest.builder()
            .nextManagerId(user2.getUserId())
            .build();

        UpdateChatroomManagerRequest request2 = UpdateChatroomManagerRequest.builder()
            .nextManagerId(user1.getUserId())
            .build();

        Chat chat = Chat.builder()
            .chatroomId(chatroomId)
            .chatType(ChatType.CHANGE_MANAGER)
            .data(ChatUtil.getChangeManagerDataFromNickname(nextManager.getUser().getNickname()))
            .build();

        @Test
        @DisplayName("채팅방 참여자가 아니면 예외가 발생한다.")
        void ifNotAChatroomParticipantThenThrowException() {
            //given
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user1.getUserId(), chatroomId)).thenReturn(Optional.empty());
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user2.getUserId(), chatroomId)).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> chatroomService.updateChatroomManager(user1.getUserId(), chatroomId, request1)).isInstanceOf(NotAChatroomParticipantException.class);
            assertThatThrownBy(() -> chatroomService.updateChatroomManager(user2.getUserId(), chatroomId, request2)).isInstanceOf(NotAChatroomParticipantException.class);
        }

        @Test
        @DisplayName("기존 채팅방 매니저를 다음 매니저로 선택할 경우 예외가 발생한다.")
        void alreadyChatroomManagerException() {
            //given
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user1.getUserId(), chatroomId)).thenReturn(Optional.of(currentManager));

            //then
            assertThatThrownBy(() -> chatroomService.updateChatroomManager(user1.getUserId(), chatroomId, request2))
                .isInstanceOf(AlreadyChatroomManagerException.class);
        }

        @Test
        @DisplayName("존재하지 않는 채팅방이면 예외가 발생한다.")
        void NotFoundChatroomException() {
            //given
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user1.getUserId(), chatroomId)).thenReturn(Optional.of(currentManager));
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user2.getUserId(), chatroomId)).thenReturn(Optional.of(nextManager));
            when(chatroomQuery.findById(chatroomId)).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> chatroomService.updateChatroomManager(user1.getUserId(), chatroomId, request1))
                .isInstanceOf(NotFoundChatroomException.class);
        }

        @Test
        @DisplayName("현재 채팅방 매니저의 role을 참여자로 변경하고 다음 채팅방 매니저의 role을 매니저로 변경한다.")
        void changeRoleOfCurrentAndNextManager() {
            //given
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user1.getUserId(), chatroomId)).thenReturn(Optional.of(currentManager));
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user2.getUserId(), chatroomId)).thenReturn(Optional.of(nextManager));
            when(chatroomQuery.findById(chatroomId)).thenReturn(Optional.of(chatroom));
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.updateChatroomManager(user1.getUserId(), chatroomId, request1);

            //then
            assertThat(currentManager.getRole()).isEqualTo(ChatroomParticipantRole.PARTICIPANT);
            assertThat(nextManager.getRole()).isEqualTo(ChatroomParticipantRole.MANAGER);
        }

        @Test
        @DisplayName("채팅방의 매니저를 새로운 매니저로 변경한다.")
        void changeChatroomManagerToNextManager() {
            //given
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user1.getUserId(), chatroomId)).thenReturn(Optional.of(currentManager));
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user2.getUserId(), chatroomId)).thenReturn(Optional.of(nextManager));
            when(chatroomQuery.findById(chatroomId)).thenReturn(Optional.of(chatroom));
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.updateChatroomManager(user1.getUserId(), chatroomId, request1);

            //then
            assertThat(chatroom.getManager()).usingRecursiveComparison().isEqualTo(nextManager);
        }

        @Test
        @DisplayName("방장 변경 유형의 채팅을 저장하고 채팅방에 메시지를 전송한다.")
        void saveChatAndPublish() {
            //given
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user1.getUserId(), chatroomId)).thenReturn(Optional.of(currentManager));
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user2.getUserId(), chatroomId)).thenReturn(Optional.of(nextManager));
            when(chatroomQuery.findById(chatroomId)).thenReturn(Optional.of(chatroom));
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.updateChatroomManager(user1.getUserId(), chatroomId, request1);

            //then
            ArgumentCaptor<Chat> chatArgumentCaptor = ArgumentCaptor.forClass(Chat.class);
            verify(chatCommand).save(chatArgumentCaptor.capture());
            assertThat(chatArgumentCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("chatId.sortId")
                .isEqualTo(Chat.builder()
                    .chatroomId(chatroom.getId())
                    .chatType(ChatType.CHANGE_MANAGER)
                    .data(ChatUtil.getChangeManagerDataFromNickname(nextManager.getUser().getNickname()))
                .build());

            ArgumentCaptor<ChatMessageDto> chatMessageDtoArgumentCaptor = ArgumentCaptor.forClass(ChatMessageDto.class);
            verify(redisPublisher).publish(eq(channelTopic), chatMessageDtoArgumentCaptor.capture());
            assertThat(chatMessageDtoArgumentCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(ChatMessageDto.of(chat, null, null));
        }
    }

    @Nested
    @DisplayName("getChatroomInfo 메서드는")
    class GetChatroomInfo {
        Chatroom chatroom = Chatroom.builder()
            .hashTagList(List.of("해시태그 1", "해시태그 2"))
            .name("채팅방 이름")
            .description("채팅방 설명")
            .build();

        @BeforeEach
        void setup() throws NoSuchFieldException, IllegalAccessException {
            Field chatroomIdField = Chatroom.class.getDeclaredField("id");
            chatroomIdField.setAccessible(true);
            chatroomIdField.set(chatroom, 123L);
        }

        @Test
        @DisplayName("ChatroomQuery의 findByChatroomIdAsSimpleResponse 메서드를 호출한다.")
        void ofMethod() {
            when(chatroomQuery.findByChatroomIdAsSimpleResponse(chatroom.getId())).thenReturn(null);

            //when
            chatroomService.getChatroomInfo(chatroom.getId());

            //then
            verify(chatroomQuery).findByChatroomIdAsSimpleResponse(chatroom.getId());
        }
    }

    @Nested
    @DisplayName("sendChatImage 메서드는")
    class SendChatImage {
        File userImage = File.builder()
            .fileUrl("userImageUrl")
            .build();
        User user = User.builder()
            .userImage(userImage)
            .build();
        Long chatroomId = 123L;
        Chat imageChat = Chat.builder()
            .chatroomId(chatroomId)
            .senderId(user.getUserId().getValue())
            .chatType(ChatType.IMAGE)
            .data("chatImageUrl")
            .build();

        @Test
        @DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
        void notFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> chatroomService.sendChatImage(user.getUserId(), chatroomId, multipartFile))
                .isInstanceOf(NotFoundUserException.class);
        }

        @Test
        @DisplayName("fileService 의 saveFileByMultipartFile를 호출한다.")
        void callSaveFileByMultipartFile() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(file);
            when(chatCommand.save(any(Chat.class))).thenReturn(imageChat);

            //when
            chatroomService.sendChatImage(user.getUserId(), chatroomId, multipartFile);

            //then
            verify(fileService).saveFileByMultipartFile(multipartFile, user.getUserId());
        }

        @Test
        @DisplayName("chatCommand 의 save를 호출한다.")
        void callSave() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(file);
            when(chatCommand.save(any(Chat.class))).thenReturn(imageChat);

            //when
            chatroomService.sendChatImage(user.getUserId(), chatroomId, multipartFile);

            //then
            verify(chatCommand).save(any(Chat.class));
        }

        @Test
        @DisplayName("redisPublisher의 publish 메서드를 호출한다.")
        void callPublish() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(file);
            when(chatCommand.save(any(Chat.class))).thenReturn(imageChat);
            ChatMessageDto expectedResult = ChatMessageDto.of(
                imageChat,
                user.getNickname(),
                user.getUserImage().getFileUrl()
            );

            //when
            chatroomService.sendChatImage(user.getUserId(), chatroomId, multipartFile);

            //then
            ArgumentCaptor<ChatMessageDto> argumentCaptor = ArgumentCaptor.forClass(ChatMessageDto.class);
            verify(redisPublisher).publish(eq(channelTopic), argumentCaptor.capture());
            assertThat(argumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedResult);
        }
    }

    @Nested
    @DisplayName("createChatroom 메서드는")
    class CreateChatroom {
        @Test
        @DisplayName("유저가 존재하지않으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> chatroomService.createChatroom(user.getUserId(), request, multipartFile)).isInstanceOf(NotFoundUserException.class);

            //then
            verify(userQuery).findByUserId(user.getUserId());
        }

        @Test
        @DisplayName("생성된 chatroom의 mulitparFile이 null이면 fileService의 saveFileByMultipartFile메서드를 호출하지 않는다.")
        void nullNotCallSaveFileByMultipartFile() {
            //given
            kr.co.pawpaw.mysql.chatroom.domain.Chatroom chatroom = request.toChatroom(file);
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomCommand.save(any(kr.co.pawpaw.mysql.chatroom.domain.Chatroom.class))).thenReturn(chatroom);
            //when
            chatroomService.createChatroom(user.getUserId(), request, null);
            //then
            verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
        }

        @Test
        @DisplayName("생성된 chatroom의 mulitparFile의 길이가 0이면 fileService의 saveFileByMultipartFile메서드를 호출하지 않는다.")
        void zeroLenNotCallSaveFileByMultipartFile() throws IOException {
            //given
            kr.co.pawpaw.mysql.chatroom.domain.Chatroom chatroom = request.toChatroom(file);
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomCommand.save(any(kr.co.pawpaw.mysql.chatroom.domain.Chatroom.class))).thenReturn(chatroom);
            when(multipartFile.getBytes()).thenReturn(new byte[0]);

            //when
            chatroomService.createChatroom(user.getUserId(), request, multipartFile);

            //then
            verify(fileService, times(0)).saveFileByMultipartFile(any(), any());
        }

        @Test
        @DisplayName("생성된 chatroom의 mulitparFile이 정상이면 생성된 chatroom의 Id를 필드로 가지는 CreateChatroomResponse를 반환한다.")
        void returnCreateChatroomResponse() throws IllegalAccessException, IOException, NoSuchFieldException {
            //given
            kr.co.pawpaw.mysql.chatroom.domain.Chatroom chatroom = request.toChatroom(file);
            Field idField = chatroom.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            Long id = 1234L;
            idField.set(chatroom, id);
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomCommand.save(any(kr.co.pawpaw.mysql.chatroom.domain.Chatroom.class))).thenReturn(chatroom);
            when(multipartFile.getBytes()).thenReturn(new byte[file.getByteSize().intValue()]);
            when(fileService.saveFileByMultipartFile(multipartFile, user.getUserId())).thenReturn(file);
            //when
            CreateChatroomResponse response = chatroomService.createChatroom(user.getUserId(), request, multipartFile);

            //then
            assertThat(response.getChatroomId()).isEqualTo(id);
        }
    }

    @Nested
    @DisplayName("createChatroomWithDefaultCover 메서드는")
    class CreateChatroomWithDefaultCover {
        @Test
        @DisplayName("유저가 존재하지않으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> chatroomService.createChatroomWithDefaultCover(user.getUserId(), requestWithDefaultCover)).isInstanceOf(NotFoundUserException.class);

            //then
            verify(userQuery).findByUserId(user.getUserId());
        }

        @Test
        @DisplayName("ChatroomDefaultCover가 존재하지 않으면 예외가 발생한다.")
        void notFoundChatroomDefaultCoverException() {
            //given
            Long chatroomDefaultCoverId = 12345L;
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomDefaultCoverQuery.findById(chatroomDefaultCoverId)).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> chatroomService.createChatroomWithDefaultCover(user.getUserId(), requestWithDefaultCover)).isInstanceOf(NotFoundChatroomDefaultCoverException.class);

            //then
        }

        @Test
        @DisplayName("ChatroomDefaultCover가 존재하면 생성된 chatroom의 Id를 필드로 가지는 CreateChatroomResponse를 반환한다.")
        void returnCreateChatroomResponse() throws NoSuchFieldException, IllegalAccessException {
            //given
            Chatroom chatroom = requestWithDefaultCover.toChatroom(file);
            Field idField = chatroom.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(chatroom, requestWithDefaultCover.getCoverId());
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomDefaultCoverQuery.findById(requestWithDefaultCover.getCoverId())).thenReturn(Optional.of(cover));
            when(chatroomCommand.save(any(Chatroom.class))).thenReturn(chatroom);

            //when
            CreateChatroomResponse response = chatroomService.createChatroomWithDefaultCover(user.getUserId(), requestWithDefaultCover);

            //then
            assertThat(response.getChatroomId()).isEqualTo(requestWithDefaultCover.getCoverId());
        }
    }

    @Nested
    @DisplayName("getChatroomDefaultCoverList 메서드는")
    class GetChatroomDefaultCoverList {
        @Test
        @DisplayName("chatroomDefaultCoverQuery의 findAllChatroomCover 메서드를 호출하고 반환값을 그대로 반환한다.")
        void findAllChatroomCover() {
            //given
            List<ChatroomCoverResponse> responseList = List.of(
                new ChatroomCoverResponse(12345L, "coverUrl1"),
                new ChatroomCoverResponse(123456L, "coverUrl2")
            );
            when(chatroomDefaultCoverQuery.findAllChatroomCover()).thenReturn(responseList);

            //when
            List<ChatroomCoverResponse> result = chatroomService.getChatroomDefaultCoverList();

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(responseList);
            verify(chatroomDefaultCoverQuery).findAllChatroomCover();
        }
    }

    @Nested
    @DisplayName("joinChatroom 메서드는")
    class JoinChatroom {
        @Test
        @DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> chatroomService.joinChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(NotFoundUserException.class);

            //then
        }
        @Test
        @DisplayName("채팅방에 이미 참여했으면 예외가 발생한다.")
        void alreadyChatroomParticipantException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(true);
            //when
            assertThatThrownBy(() -> chatroomService.joinChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(AlreadyChatroomParticipantException.class);

            //then
        }

        @Test
        @DisplayName("채팅방에 참여하지 않았으면 participant role로 참여자를 생성한다.")
        void success() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(false);
            when(trendingChatroomQuery.existsByChatroomId(chatroom.getId())).thenReturn(true);
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.joinChatroom(user.getUserId(), chatroom.getId());

            //then
            ArgumentCaptor<ChatroomParticipant> chatroomParticipantArgumentCaptor = ArgumentCaptor.forClass(ChatroomParticipant.class);
            verify(chatroomParticipantCommand).save(chatroomParticipantArgumentCaptor.capture());
            assertThat(chatroomParticipantArgumentCaptor.getValue().isManager()).isFalse();
        }

        @Test
        @DisplayName("참여한 채팅방을 뜨고있는 채팅방으로 설정한다.")
        void setTrendingChatroom() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(false);
            when(trendingChatroomQuery.existsByChatroomId(chatroom.getId())).thenReturn(false);
            when(chatroomQuery.getReferenceById(chatroom.getId())).thenReturn(chatroom);
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.joinChatroom(user.getUserId(), chatroom.getId());

            //then
            ArgumentCaptor<TrendingChatroom> trendingChatroomArgumentCaptor = ArgumentCaptor.forClass(TrendingChatroom.class);
            verify(trendingChatroomCommand).save(trendingChatroomArgumentCaptor.capture());
            assertThat(trendingChatroomArgumentCaptor.getValue().getChatroom().getId()).isEqualTo(chatroom.getId());
        }

        @Test
        @DisplayName("입장 채팅을 저장하고 redis에 publish한다.")
        void saveChatAndPublishMessage() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.existsByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(false);
            when(trendingChatroomQuery.existsByChatroomId(chatroom.getId())).thenReturn(false);
            when(chatroomQuery.getReferenceById(chatroom.getId())).thenReturn(chatroom);
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.joinChatroom(user.getUserId(), chatroom.getId());

            //then
            verify(chatCommand).save(any(Chat.class));
            ArgumentCaptor<ChatMessageDto> argumentCaptor = ArgumentCaptor.forClass(ChatMessageDto.class);
            verify(redisPublisher).publish(eq(channelTopic), argumentCaptor.capture());
            assertThat(argumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(ChatMessageDto.of(chat, null, null));
        }
    }

    @Nested
    @DisplayName("leaveChatroom 메서드는")
    class LeaveChatroom {
        @Test
        @DisplayName("유저가 존재하지 않으면 예외가 발생한다.")
        void notFoundUserException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.empty());
            //when
            assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(NotFoundUserException.class);

            //then
        }

        @Test
        @DisplayName("채팅방 참여자가 아니면 예외가 발생한다.")
        void notChatroomParticipantException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(Optional.empty());

            //when
            assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(IsNotChatroomParticipantException.class);

            //then
        }

        @Test
        @DisplayName("채팅방 매니저는 채팅방에서 나갈 수 없다.")
        void managerNotAllowedChatroomLeaveException() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(Optional.of(ChatroomParticipant.builder()
                .role(ChatroomParticipantRole.MANAGER)
                .build()));

            //when
            assertThatThrownBy(() -> chatroomService.leaveChatroom(user.getUserId(), chatroom.getId())).isInstanceOf(NotAllowedChatroomLeaveException.class);

            //then
        }

        @Test
        @DisplayName("chatroomParticipantCommand의 delete메서드를 호출한다.")
        void callDeleteMethod() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            ChatroomParticipant chatroomParticipant = ChatroomParticipant.builder()
                .role(ChatroomParticipantRole.PARTICIPANT)
                .build();
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(Optional.of(chatroomParticipant));
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.leaveChatroom(user.getUserId(), chatroom.getId());

            //then
            verify(chatroomParticipantCommand).delete(chatroomParticipant);
        }

        @Test
        @DisplayName("입장 채팅을 저장하고 redis에 publish한다.")
        void saveChatAndPublishMessage() {
            //given
            when(userQuery.findByUserId(user.getUserId())).thenReturn(Optional.of(user));
            ChatroomParticipant chatroomParticipant = ChatroomParticipant.builder()
                .role(ChatroomParticipantRole.PARTICIPANT)
                .build();
            when(chatroomParticipantQuery.findByUserIdAndChatroomId(user.getUserId(), chatroom.getId())).thenReturn(Optional.of(chatroomParticipant));
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.leaveChatroom(user.getUserId(), chatroom.getId());

            //then
            verify(chatCommand).save(any(Chat.class));
            ArgumentCaptor<ChatMessageDto> argumentCaptor = ArgumentCaptor.forClass(ChatMessageDto.class);
            verify(redisPublisher).publish(eq(channelTopic), argumentCaptor.capture());
            assertThat(argumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(ChatMessageDto.of(chat, null, null));
        }
    }

    @Nested
    @DisplayName("findBeforeChatMessages 메서드는")
    class FindBeforeChatMessages {
        Long chatroomId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        List<Chat> chatList = List.of(Chat.builder()
            .chatroomId(chatroomId)
            .chatType(ChatType.MESSAGE)
            .senderId("senderId1")
            .data("data1")
            .build(),
            Chat.builder()
                .chatroomId(chatroomId)
                .chatType(ChatType.JOIN)
                .data("hi 님이 입장하셨습니다.")
                .build());
        Slice<Chat> chatSlice = new SliceImpl<>(chatList, pageable, true);
        List<ChatMessageUserDto> chatMessageUserDtos = List.of(
            new ChatMessageUserDto(UserId.of(chatList.get(0).getSenderId()), "nickname", "imageUrl")
        );
        @Test
        @DisplayName("전송자 아이디가 존재하면 전송자의 닉네임과 이미지 URL을 포함하는 dto를 전송한다.")
        void sendWithNicknameAndImageUrlIfSenderIdExists() {
            //given
            when(chatQuery.findWithSliceByChatroomIdAndSortIdLessThan(eq(chatroomId), eq(null), any(Pageable.class))).thenReturn(chatSlice);
            when(userQuery.getChatMessageUserDtoByUserIdIn(any(List.class))).thenReturn(chatMessageUserDtos);
            Slice<ChatMessageDto> resultExpected = chatSlice.map(chat -> ChatMessageDto.of(chat, Objects.isNull(chat.getSenderId()) ? null : "nickname", Objects.isNull(chat.getSenderId()) ? null : "imageUrl"));

            //when
            Slice<ChatMessageDto> result = chatroomService.findBeforeChatMessages(chatroomId, null, 2);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
        }
    }

    @Nested
    @DisplayName("getParticipatedChatroomList 메서드는")
    class GetParticipatedChatroomList {
        Long chatroomId = 1L;
        UserId userId = UserId.create();
        Chat lastChat = Chat.builder()
            .senderId("senderId")
            .chatroomId(chatroomId)
            .chatType(ChatType.MESSAGE)
            .data("메시지입니다.")
            .build();

        @BeforeEach
        void setup() throws NoSuchFieldException, IllegalAccessException {
            Field createdDate = Chat.class.getDeclaredField("createdDate");
            createdDate.setAccessible(true);
            createdDate.set(lastChat, LocalDateTime.now().minusDays(1));
        }


        @Test
        @DisplayName("chatroomQuery의 getParticipatedChatroomDetailDataByUserId 메서드를 호출한다.")
        void callGetParticipatedChatroomDetailDataByUserId() {
            //given
            List<ChatroomDetailData> chatroomDetailData = List.of(
                new ChatroomDetailData(
                    chatroomId,
                    "name",
                    "description",
                    "coverUrl",
                    List.of("hashTag1", "hashTag2"),
                    "managerName",
                    "managerImageUrl",
                    2L,
                    false,
                    false
                )
            );

            List<ChatroomDetailResponse> resultExpected = chatroomDetailData.stream()
                .map(data -> ChatroomDetailResponse.of(data, lastChat.getCreatedDate()))
                .collect(Collectors.toList());

            when(chatroomQuery.getParticipatedChatroomDetailDataByUserId(userId)).thenReturn(chatroomDetailData);
            when(chatQuery.findFirstByChatroomIdOrderBySortIdDesc(chatroomId)).thenReturn(Optional.of(lastChat));

            //when
            List<ChatroomDetailResponse> result = chatroomService.getParticipatedChatroomList(userId);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
            verify(chatroomQuery).getParticipatedChatroomDetailDataByUserId(userId);
        }
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
    @DisplayName("getTrendingChatroomList 메서드는 chatroomQuery의 getAccessibleTrendingChatroom를 호출한다.")
    void getTrendingChatroomList() {
        //given
        UserId userId = UserId.create();
        Long beforeId = null;
        int size = 10;

        when(chatroomQuery.getAccessibleTrendingChatroom(userId, beforeId, size)).thenReturn(new SliceImpl<>(
            List.of(), PageRequest.of(0, 10),  false
        ));

        //when
        chatroomService.getTrendingChatroomList(userId, beforeId, size);

        //then
        verify(chatroomQuery).getAccessibleTrendingChatroom(userId, beforeId, size);
    }

    @Nested
    @DisplayName("getChatroomParticipantResponseList 메서드는")
    class GetChatroomParticipantResponseList {
        String defaultImageUrl = "기본 이미지 URL";
        Long chatroomId = 12345L;
        UserId userId = UserId.create();
        List<ChatroomParticipantResponse> nullResponseList = List.of(
            new ChatroomParticipantResponse(
                userId,
                "nickname",
                "briefIntroduction",
                null,
                ChatroomParticipantRole.PARTICIPANT
            )
        );
        List<ChatroomParticipantResponse> nonNullResponseList = nullResponseList.stream()
            .map(response -> new ChatroomParticipantResponse(
                response.getUserId(),
                response.getNickname(),
                response.getBriefIntroduction(),
                defaultImageUrl,
                response.getRole()
            )).collect(Collectors.toList());

        @Test
        @DisplayName("ChatroomParticipantResponse의 ImageUrl이 null이면 기본 이미지 URL로 변경한다.")
        void changeDefaultImageUrl() {
            //given
            when(userService.getUserDefaultImageUrl()).thenReturn(defaultImageUrl);
            when(chatroomParticipantQuery.getChatroomParticipantResponseList(chatroomId)).thenReturn(nullResponseList);

            //when
            List<ChatroomParticipantResponse> result = chatroomService.getChatroomParticipantResponseList(chatroomId);

            //then
            assertThat(nonNullResponseList).usingRecursiveComparison().isEqualTo(result);
        }
    }

    @Nested
    @DisplayName("searchChatroomNonParticipants 메서드는")
    class SearchChatroomNonParticipants {
        String defaultImageUrl = "기본 이미지 URL";
        Long chatroomId = 1234L;
        String nicknameKeyword = "nickname";
        List<ChatroomNonParticipantResponse> nullResponseList = List.of(
            new ChatroomNonParticipantResponse(
                UserId.create(),
                "nickname",
                "briefIntroduction",
                null
            )
        );

        List<ChatroomNonParticipantResponse> nonNullResponseList = nullResponseList
            .stream()
            .map(response -> new ChatroomNonParticipantResponse(
                response.getUserId(),
                response.getNickname(),
                response.getBriefIntroduction(),
                defaultImageUrl
            )).collect(Collectors.toList());

        @Test
        @DisplayName("ChatroomNonParticipantResponse의 ImageUrl이 null이면 기본 이미지 URL로 변경한다.")
        void changeDefaultImageUrl() {
            //given
            when(userService.getUserDefaultImageUrl()).thenReturn(defaultImageUrl);
            when(userQuery.searchChatroomNonParticipant(chatroomId, nicknameKeyword)).thenReturn(nullResponseList);


            //when
            List<ChatroomNonParticipantResponse> result = chatroomService.searchChatroomNonParticipants(chatroomId, nicknameKeyword);

            //then
            assertThat(result).usingRecursiveComparison().isEqualTo(nonNullResponseList);
        }
    }

    @Nested
    @DisplayName("inviteUser 메서드는")
    class InviteUser {
        private Long chatroomId = 123L;
        private Chatroom chatroom = Chatroom.builder()
            .name("chatroom-name")
            .description("chatroom-description")
            .hashTagList(List.of("hashtag-1", "hashtag-2"))
            .searchable(true)
            .locationLimit(false)
            .build();
        private User user = User.builder()
            .email("user-email")
            .password("user-password")
            .name("user-name")
            .nickname("user-nickname")
            .briefIntroduction("user-briefIntroduction")
            .phoneNumber("user-phoneNumber")
            .build();
        private InviteChatroomUserRequest request = InviteChatroomUserRequest.builder()
            .userId(user.getUserId())
            .build();
        private ChatroomParticipant chatroomParticipant = ChatroomParticipant.builder()
            .chatroom(chatroom)
            .user(user)
            .role(ChatroomParticipantRole.PARTICIPANT)
            .build();

        @Test
        @DisplayName("존재하지 않는 유저를 초대하면 예외가 발생한다.")
        void NotFoundUserException() {
            //given
            when(userQuery.findByUserId(request.getUserId())).thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> chatroomService.inviteUser(chatroomId, request))
                .isInstanceOf(NotFoundUserException.class);
        }

        @Test
        @DisplayName("이미 참여한 유저를 초대하면 예외가 발생한다.")
        void AlreadyChatroomParticipantException() {
            //given
            when(userQuery.findByUserId(request.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.existsByUserIdAndChatroomId(request.getUserId(), chatroomId)).thenReturn(true);

            //then
            assertThatThrownBy(() -> chatroomService.inviteUser(chatroomId, request))
                .isInstanceOf(AlreadyChatroomParticipantException.class);
        }

        @Test
        @DisplayName("ChatroomParticipantCommand의 save메서드를 호출한다.")
        void callSaveMethod() {
            //given
            when(userQuery.findByUserId(request.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.existsByUserIdAndChatroomId(request.getUserId(), chatroomId)).thenReturn(false);
            when(chatroomQuery.getReferenceById(chatroomId)).thenReturn(chatroom);
            when(userQuery.getReferenceById(request.getUserId())).thenReturn(user);
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.inviteUser(chatroomId, request);

            //then
            ArgumentCaptor<ChatroomParticipant> captor = ArgumentCaptor.forClass(ChatroomParticipant.class);
            verify(chatroomParticipantCommand).save(captor.capture());
            assertThat(captor.getValue()).usingRecursiveComparison().isEqualTo(chatroomParticipant);
        }

        @Test
        @DisplayName("chatCommand의 save 메서드를 통해 invite 유형의 chat을 저장하고 redisPublisher의 publish를 호출하여 채팅방에 메시지를 전송한다.")
        void saveChatAndPublishInviteMessage() {
            //given
            when(userQuery.findByUserId(request.getUserId())).thenReturn(Optional.of(user));
            when(chatroomParticipantQuery.existsByUserIdAndChatroomId(request.getUserId(), chatroomId)).thenReturn(false);
            when(chatroomQuery.getReferenceById(chatroomId)).thenReturn(chatroom);
            when(userQuery.getReferenceById(request.getUserId())).thenReturn(user);
            when(chatCommand.save(any(Chat.class))).thenReturn(chat);

            //when
            chatroomService.inviteUser(chatroomId, request);

            //then
            ArgumentCaptor<Chat> argumentCaptor = ArgumentCaptor.forClass(Chat.class);
            verify(chatCommand).save(argumentCaptor.capture());
            assertThat(argumentCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("chatId.sortId")
                .isEqualTo(Chat.builder()
                    .chatroomId(chatroomId)
                    .chatType(ChatType.INVITE)
                    .data(ChatUtil.getInviteDataFromNickname(user.getNickname()))
                    .build());
            ArgumentCaptor<ChatMessageDto> chatMessageDtoArgumentCaptor = ArgumentCaptor.forClass(ChatMessageDto.class);
            verify(redisPublisher).publish(eq(channelTopic), chatMessageDtoArgumentCaptor.capture());
            assertThat(chatMessageDtoArgumentCaptor.getValue()).usingRecursiveComparison()
                .isEqualTo(ChatMessageDto.of(chat, null, null));
        }
    }
}