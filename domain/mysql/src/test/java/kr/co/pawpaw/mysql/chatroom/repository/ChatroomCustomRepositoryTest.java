package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.*;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomDetailData;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomResponse;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@DisplayName("ChatroomCustomRepository의")
@Import(value = { ChatroomCustomRepository.class, QuerydslConfig.class })
@DataJpaTest
class ChatroomCustomRepositoryTest {
    @Autowired
    private ChatroomCustomRepository chatroomCustomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private ChatroomScheduleRepository chatroomScheduleRepository;

    User user1 = User.builder()
        .name("user-name-1")
        .nickname("user-nickname-1")
        .phoneNumber("user-phoneNumber-1")
        .email("email1@liame.com")
        .build();
    User user2 = User.builder()
        .name("user-name-2")
        .nickname("user-nickname-2")
        .phoneNumber("user-phoneNumber-2")
        .email("email2@liame.com")
        .build();

    User user3 = User.builder()
        .name("user-name-3")
        .nickname("user-nickname-3")
        .phoneNumber("user-phoneNumber-3")
        .email("email3@liame.com")
        .build();

    File coverFile = File.builder()
        .fileName(UUID.randomUUID().toString())
        .byteSize(123L)
        .contentType("coverFile-contentType")
        .fileUrl("coverFile-url")
        .uploader(user1)
        .build();

    Chatroom chatroom = Chatroom.builder()
        .name("chatroom-name")
        .description("chatroom-description")
        .locationLimit(false)
        .searchable(true)
        .hashTagList(List.of("hashtag-1", "hashtag-2"))
        .coverFile(coverFile)
        .build();

    Chatroom chatroom2 = Chatroom.builder()
        .name("chatroom2-name")
        .description("chatroom2-description")
        .locationLimit(false)
        .searchable(true)
        .hashTagList(List.of("hashtag-1", "hashtag-2"))
        .coverFile(coverFile)
        .build();

    Chatroom chatroom3 = Chatroom.builder()
        .name("chatroom3-name")
        .description("chatroom3-description")
        .locationLimit(false)
        .searchable(false)
        .hashTagList(List.of("hashtag-1", "hashtag-2"))
        .coverFile(coverFile)
        .build();

    File managerImageFile = File.builder()
        .fileName(UUID.randomUUID().toString())
        .byteSize(1234L)
        .contentType("managerImageFile-contentType")
        .fileUrl("managerImageFile-url")
        .uploader(user1)
        .build();
    Chat chat1 = Chat.builder()
        .chatroom(chatroom)
        .data("chat-data-1")
        .sender(user1)
        .type(ChatType.MESSAGE)
        .build();

    Chat chat2 = Chat.builder()
        .chatroom(chatroom)
        .data("chat-data-2")
        .sender(user2)
        .type(ChatType.MESSAGE)
        .build();

    ChatroomParticipant manager = ChatroomParticipant.builder()
        .role(ChatroomParticipantRole.MANAGER)
        .chatroom(chatroom)
        .user(user1)
        .build();

    ChatroomParticipant participant = ChatroomParticipant.builder()
        .role(ChatroomParticipantRole.PARTICIPANT)
        .chatroom(chatroom)
        .user(user2)
        .build();

    ChatroomParticipant manager2 = ChatroomParticipant.builder()
        .role(ChatroomParticipantRole.MANAGER)
        .chatroom(chatroom2)
        .user(user1)
        .build();

    ChatroomParticipant participant2 = ChatroomParticipant.builder()
        .role(ChatroomParticipantRole.PARTICIPANT)
        .chatroom(chatroom2)
        .user(user2)
        .build();

    ChatroomParticipant manager3 = ChatroomParticipant.builder()
        .role(ChatroomParticipantRole.MANAGER)
        .chatroom(chatroom3)
        .user(user3)
        .build();

    ChatroomSchedule chatroomSchedule1 = ChatroomSchedule.builder()
        .creator(user1)
        .chatroom(chatroom)
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(1))
        .description("chatroomSchedule1-description")
        .name("chatroomSchedule1-name")
        .build();

    @BeforeEach
    void setup() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        coverFile = fileRepository.save(coverFile);
        managerImageFile = fileRepository.save(managerImageFile);
        user1.updateImage(managerImageFile);
        user1 = userRepository.save(user1);
        user3.updateImage(managerImageFile);
        user3 = userRepository.save(user3);
        chatroom = chatroomRepository.save(chatroom);
        chatroom2 = chatroomRepository.save(chatroom2);
        chatroom3 = chatroomRepository.save(chatroom3);
        chat1 = chatRepository.save(chat1);
        chat2 = chatRepository.save(chat2);
        manager = chatroomParticipantRepository.save(manager);
        participant = chatroomParticipantRepository.save(participant);
        manager2 = chatroomParticipantRepository.save(manager2);
        participant2 = chatroomParticipantRepository.save(participant2);
        manager3 = chatroomParticipantRepository.save(manager3);
        chatroom.updateManager(manager);
        chatroom2.updateManager(manager2);
        chatroom3.updateManager(manager3);
        chatroom = chatroomRepository.save(chatroom);
        chatroom2 = chatroomRepository.save(chatroom2);
        chatroom3 = chatroomRepository.save(chatroom3);
        chatroomSchedule1 = chatroomScheduleRepository.save(chatroomSchedule1);
    }

    @Test
    @DisplayName("findAllByUserIdWithDetailInfo메서드 참여중인 채팅방만 조회")
    void findAllByUserIdWithDetailInfoParticipateTest() {
        assertThat(chatroomCustomRepository.findAllByUserIdWithDetailData(user1.getUserId()).size()).isEqualTo(2);
        assertThat(chatroomCustomRepository.findAllByUserIdWithDetailData(UserId.create()).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("findAllByUserIdWithDetailInfo 메서드 응답 필드 확인")
    void findAllByUserIdWithDetailInfoFieldTest() {
        ChatroomDetailData expectedResult = new ChatroomDetailData(
            chatroom.getId(),
            chatroom.getName(),
            chatroom.getDescription(),
            coverFile.getFileUrl(),
            chat2.getCreatedDate().withNano((int)Math.round(chat2.getCreatedDate().getNano() / 1000.0) * 1000),
            chatroom.getHashTagList(),
            user1.getNickname(),
            managerImageFile.getFileUrl(),
            2L,
            false,
            true
        );

        ChatroomDetailData result = chatroomCustomRepository.findAllByUserIdWithDetailData(user1.getUserId()).get(0);

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findAllByUserIdWithDetailInfo 메서드 채팅방 순서 테스트")
    void findAllByUserIdWithDetailInfoOrderTest() {
        List<ChatroomDetailData> result = chatroomCustomRepository.findAllByUserIdWithDetailData(user1.getUserId());

        assertThat(result.get(0).getId()).isEqualTo(chatroom.getId());
        assertThat(result.get(1).getId()).isEqualTo(chatroom2.getId());
    }

    @Nested
    @DisplayName("findAccessibleNewChatroomByUserId 메서드는")
    class FindAccessibleNewChatroomByUserId {
        @Nested
        @DisplayName("참여 안한 채팅방만")
        class OnlyNotParticipatedChatroom {
            @Test
            @DisplayName("조회한다.")
            void search() {
                assertThat(chatroomCustomRepository.findAccessibleNewChatroomByUserId(user1.getUserId()).size())
                    .isEqualTo(0);

                assertThat(chatroomCustomRepository.findAccessibleNewChatroomByUserId(UserId.of(UUID.randomUUID().toString())).size())
                    .isEqualTo(2);
            }
        }

        @Nested
        @DisplayName("임의의 순서대로")
        class RandomOrder {
            Set<String> previousResult = new HashSet<>();
            @Test
            @DisplayName("조회한다.")
            void search() {
                for (int i = 0; i < 20; ++i) {
                    previousResult.add(chatroomCustomRepository.findAccessibleNewChatroomByUserId(UserId.of(UUID.randomUUID().toString()))
                        .stream()
                        .map(ChatroomResponse::getId)
                        .map(String::valueOf)
                        .collect(Collectors.joining("<>")));
                }

                assertThat(previousResult.size() > 1).isTrue();
            }
        }

        @Nested
        @DisplayName("응답 필드")
        class Result {
            @Test
            @DisplayName("확인")
            void check() {
                ChatroomResponse expectedResult = new ChatroomResponse(
                    chatroom.getId(),
                    chatroom.getName(),
                    chatroom.getDescription(),
                    chatroom.getHashTagList(),
                    user1.getNickname(),
                    managerImageFile.getFileUrl(),
                    2L
                );

                ChatroomResponse result = chatroomCustomRepository.findAccessibleNewChatroomByUserId(UserId.of(UUID.randomUUID().toString()))
                    .stream()
                    .filter(response -> response.getId().equals(chatroom.getId()))
                    .collect(Collectors.toList()).get(0);

                assertThat(result).usingRecursiveComparison().isEqualTo(expectedResult);
            }
        }

        @Nested
        @DisplayName("searchable 필드가")
        class Searchable {
            @Test
            @DisplayName("True인 채팅방만 검색한다.")
            void isTrue() {
                List<ChatroomResponse> result = chatroomCustomRepository.findAccessibleNewChatroomByUserId(UserId.create());

                assertThat(result.size()).isEqualTo(2);
                assertThat(result.stream().map(ChatroomResponse::getId)
                    .anyMatch(id -> id.equals(chatroom.getId())))
                    .isTrue();
                assertThat(result.stream().map(ChatroomResponse::getId)
                    .anyMatch(id -> id.equals(chatroom2.getId())))
                    .isTrue();
            }
        }
    }
}