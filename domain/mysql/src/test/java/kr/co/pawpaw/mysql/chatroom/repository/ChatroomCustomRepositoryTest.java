package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.*;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomDetailData;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomResponse;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatroomCustomRepository의")
@Import(value = { ChatroomCustomRepository.class, QuerydslConfig.class })
class ChatroomCustomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private ChatroomCustomRepository chatroomCustomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private ChatroomScheduleRepository chatroomScheduleRepository;
    @Autowired
    private ChatroomHashTagRepository chatroomHashTagRepository;

    User user1 = User.builder()
        .name("user-name-1")
        .position(Position.builder()
            .address("서울특별시 강동구")
            .latitude(36.8)
            .longitude(36.7)
            .build())
        .nickname("user-nickname-1")
        .phoneNumber("user-phoneNumber-1")
        .email("email1@liame.com")
        .build();
    User user2 = User.builder()
        .name("user-name-2")
        .position(Position.builder()
            .address("서울특별시 강동구")
            .latitude(36.8)
            .longitude(36.7)
            .build())
        .nickname("user-nickname-2")
        .phoneNumber("user-phoneNumber-2")
        .email("email2@liame.com")
        .build();

    User user3 = User.builder()
        .name("user-name-3")
        .position(Position.builder()
            .address("서울특별시 강동구")
            .latitude(36.8)
            .longitude(36.7)
            .build())
        .nickname("user-nickname-3")
        .phoneNumber("user-phoneNumber-3")
        .email("email3@liame.com")
        .build();

    User nonPartipantUser = User.builder().build();

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
        .coverFile(coverFile)
        .build();

    Chatroom chatroom2 = Chatroom.builder()
        .name("chatroom2-name")
        .description("chatroom2-description")
        .locationLimit(false)
        .searchable(true)
        .coverFile(coverFile)
        .build();

    Chatroom chatroom3 = Chatroom.builder()
        .name("chatroom3-name")
        .description("chatroom3-description")
        .locationLimit(false)
        .searchable(false)
        .coverFile(coverFile)
        .build();

    List<ChatroomHashTag> hashTagList = List.of(
        ChatroomHashTag.builder()
            .chatroom(chatroom)
            .hashTag("hashtag-1")
            .build(),
        ChatroomHashTag.builder()
            .chatroom(chatroom)
            .hashTag("hashtag-2")
            .build(),
        ChatroomHashTag.builder()
            .chatroom(chatroom2)
            .hashTag("hashtag-1")
            .build(),
        ChatroomHashTag.builder()
            .chatroom(chatroom2)
            .hashTag("hashtag-2")
            .build(),
        ChatroomHashTag.builder()
            .chatroom(chatroom3)
            .hashTag("hashtag-1")
            .build(),
        ChatroomHashTag.builder()
            .chatroom(chatroom3)
            .hashTag("hashtag-2")
            .build());

    File managerImageFile = File.builder()
        .fileName(UUID.randomUUID().toString())
        .byteSize(1234L)
        .contentType("managerImageFile-contentType")
        .fileUrl("managerImageFile-url")
        .uploader(user1)
        .build();

    ChatroomParticipant manager;

    ChatroomParticipant participant;

    ChatroomParticipant manager2;

    ChatroomParticipant participant2;

    ChatroomParticipant manager3;

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
        manager = chatroomParticipantRepository.save(ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.MANAGER)
            .chatroom(chatroom)
            .user(user1)
            .build());
        participant = chatroomParticipantRepository.save(ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.PARTICIPANT)
            .chatroom(chatroom)
            .user(user2)
            .build());
        manager2 = chatroomParticipantRepository.save(ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.MANAGER)
            .chatroom(chatroom2)
            .user(user1)
            .build());
        participant2 = chatroomParticipantRepository.save(ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.PARTICIPANT)
            .chatroom(chatroom2)
            .user(user2)
            .build());
        manager3 = chatroomParticipantRepository.save(ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.MANAGER)
            .chatroom(chatroom3)
            .user(user3)
            .build());
        chatroom.updateManager(manager);
        chatroom2.updateManager(manager2);
        chatroom3.updateManager(manager3);
        chatroom = chatroomRepository.save(chatroom);
        chatroom2 = chatroomRepository.save(chatroom2);
        chatroom3 = chatroomRepository.save(chatroom3);
        chatroomSchedule1 = chatroomScheduleRepository.save(chatroomSchedule1);
        hashTagList = chatroomHashTagRepository.saveAll(hashTagList);
    }

    @Nested
    @DisplayName("findBySearchQuery 메서드는")
    @Transactional(propagation = Propagation.NEVER)
    class FindBySearchQuery {
        String nameKeyword1 = "chatroom3";
        String nameKeyword2 = "chatroom";
        String descriptionKeyword1 = "chatroom3";
        String descriptionKeyword2 = "descriptio";

        @AfterEach
        void setup() throws NoSuchFieldException, IllegalAccessException {
            chatroomHashTagRepository.deleteAll();
            chatroomScheduleRepository.deleteAll();
            chatroom.updateManager(null);
            chatroom2.updateManager(null);
            chatroom3.updateManager(null);
            chatroomRepository.saveAll(List.of(chatroom, chatroom2, chatroom3));
            chatroomParticipantRepository.deleteAll();
            chatroomRepository.deleteAll();
            Field uploaderField = coverFile.getClass().getDeclaredField("uploader");
            uploaderField.setAccessible(true);
            uploaderField.set(coverFile, null);
            uploaderField.set(managerImageFile, null);
            fileRepository.saveAll(List.of(coverFile, managerImageFile));
            userRepository.deleteAll();
            fileRepository.deleteAll();
        }

        @Test
        @DisplayName("채팅방의 검색어와 이름이 한글자 이상 일치하는 것을 검색한다.")
        void findChatroomMatchBetweenKeywordAndName() {
            //when
            List<ChatroomResponse> result1 = chatroomCustomRepository.findBySearchQuery(nameKeyword1, nonPartipantUser.getUserId(), null).getContent();
            List<ChatroomResponse> result2 = chatroomCustomRepository.findBySearchQuery(nameKeyword2, nonPartipantUser.getUserId(), null).getContent();

            //then
            assertThat(chatroomRepository.findAll().size()).isEqualTo(3);
            assertThat(result1.size()).isEqualTo(1);
            assertThat(result2.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("채팅방의 검색어와 설명이 한글자 이상 일치하는 것을 검색한다.")
        void findChatroomMatchBetweenKeywordAndDescription() {
            //when
            List<ChatroomResponse> result1 = chatroomCustomRepository.findBySearchQuery(descriptionKeyword1, nonPartipantUser.getUserId(), null).getContent();
            List<ChatroomResponse> result2 = chatroomCustomRepository.findBySearchQuery(descriptionKeyword2, nonPartipantUser.getUserId(), null).getContent();

            //then
            assertThat(chatroomRepository.findAll().size()).isEqualTo(3);
            assertThat(result1.size()).isEqualTo(1);
            assertThat(result2.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("채팅방의 검색어와 해시태그가 한글자 이상 일치하는 것을 검색한다.")
        void findChatroomMatchBetweenKeywordAndHashTag() {
            //when
            List<ChatroomResponse> result1 = chatroomCustomRepository.findBySearchQuery("1", nonPartipantUser.getUserId(), null).getContent();
            List<ChatroomResponse> result2 = chatroomCustomRepository.findBySearchQuery("2", nonPartipantUser.getUserId(), null).getContent();

            //then
            assertThat(chatroomRepository.findAll().size()).isEqualTo(3);
            assertThat(result1.size()).isEqualTo(3);
            assertThat(result2.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("검색한 유저가 참가하지 않는 채팅방만 검색된다.")
        void onlyFindNotParticipatedChatroom() {
            //when
            List<ChatroomResponse> result1 = chatroomCustomRepository.findBySearchQuery("1", user1.getUserId(), null).getContent();
            List<ChatroomResponse> result2 = chatroomCustomRepository.findBySearchQuery("1", user3.getUserId(), null).getContent();

            List<ChatroomParticipant> result = chatroomParticipantRepository.findAll();

            //then
            assertThat(result1.size()).isEqualTo(1);
            assertThat(result2.size()).isEqualTo(2);
        }

        @ParameterizedTest
        @CsvSource(value = {"1,1", "2,2", "3,3", "4,3", "null,3"}, nullValues = "null")
        @DisplayName("PageRequest에 해당하는 부분을 반환한다.")
        void returnThePartsCorrespondingToPageRequest(final Integer size, final Integer expectedResultSize) {
            //given
            PageRequest pageRequest = null;

            if (size != null) pageRequest = PageRequest.ofSize(size);

            //when
            Slice<ChatroomResponse> result = chatroomCustomRepository.findBySearchQuery("1", nonPartipantUser.getUserId(), pageRequest);

            //then
            assertThat(result.getNumberOfElements()).isEqualTo(expectedResultSize);
        }

        @ParameterizedTest
        @CsvSource(value = {"1,true", "2,true", "3,false", "4,false", "null,false"}, nullValues = "null")
        @DisplayName("hasNext로 다음 부분이 있는지 표시한다.")
        void showHasNext(final Integer size, final boolean hasNext) {
            //given
            PageRequest pageRequest = null;

            if (size != null) pageRequest = PageRequest.ofSize(size);

            //when
            Slice<ChatroomResponse> result = chatroomCustomRepository.findBySearchQuery("1", nonPartipantUser.getUserId(), pageRequest);

            //then
            assertThat(result.hasNext()).isEqualTo(hasNext);
        }

    }

    @Nested
    @DisplayName("findAllByUserIdWithDetailInfo 메서드는")
    class FindAllByUserIdWithDetailInfo {
        @Test
        @DisplayName("참여중인 채팅방만 조회한다.")
        void findAllByUserIdWithDetailInfoParticipateTest() {
            assertThat(chatroomCustomRepository.findAllByUserIdWithDetailData(user1.getUserId()).size()).isEqualTo(2);
            assertThat(chatroomCustomRepository.findAllByUserIdWithDetailData(UserId.create()).size()).isEqualTo(0);
        }

        @Test
        @DisplayName("응답값이 예상과 같다.")
        void findAllByUserIdWithDetailInfoFieldTest() {
            ChatroomDetailData expectedResult = new ChatroomDetailData(
                chatroom.getId(),
                chatroom.getName(),
                chatroom.getDescription(),
                coverFile.getFileUrl(),
                hashTagList.stream()
                    .filter(hashtag -> hashtag.getChatroom().equals(chatroom))
                    .map(ChatroomHashTag::getHashTag)
                    .collect(Collectors.toList()),
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
        @DisplayName("채팅방 순서가 보장된다.")
        void findAllByUserIdWithDetailInfoOrderTest() {
            for (int i = 0; i < 10; ++i) {
                List<ChatroomDetailData> result = chatroomCustomRepository.findAllByUserIdWithDetailData(user1.getUserId());

                assertThat(result.get(0).getId()).isEqualTo(chatroom.getId());
                assertThat(result.get(1).getId()).isEqualTo(chatroom2.getId());
            }
        }
    }

    @Nested
    @DisplayName("findAccessibleNewChatroomByUserId 메서드는")
    class FindAccessibleNewChatroomByUserId {
        Set<String> previousResult = new HashSet<>();
        @Test
        @DisplayName("참여 안한 채팅방만 조회한다.")
        void search() {
            assertThat(chatroomCustomRepository.findAccessibleNewChatroomByUserId(user1.getUserId()).size())
                .isEqualTo(0);

            assertThat(chatroomCustomRepository.findAccessibleNewChatroomByUserId(UserId.of(UUID.randomUUID().toString())).size())
                .isEqualTo(2);
        }

        @Test
        @DisplayName("임의의 순서대로 조회한다.")
        void randomSearch() {
            for (int i = 0; i < 20; ++i) {
                previousResult.add(chatroomCustomRepository.findAccessibleNewChatroomByUserId(UserId.of(UUID.randomUUID().toString()))
                    .stream()
                    .map(ChatroomResponse::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining("<>")));
            }

            assertThat(previousResult.size() > 1).isTrue();
        }

        @Test
        @DisplayName("응답 필드 확인")
        void check() {
            ChatroomResponse expectedResult = new ChatroomResponse(
                chatroom.getId(),
                chatroom.getName(),
                chatroom.getDescription(),
                hashTagList.stream()
                    .filter(hashtag -> hashtag.getChatroom().equals(chatroom))
                    .map(ChatroomHashTag::getHashTag)
                    .collect(Collectors.toList()),
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

        @Test
        @DisplayName("searchable 필드가 True인 채팅방만 검색한다.")
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