package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.*;
import kr.co.pawpaw.mysql.chatroom.dto.TrendingChatroomResponse;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.config.QuerydslConfig;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@DisplayName("TrendingChatroomCustomRepository의")
@Import(value = { TrendingChatroomCustomRepository.class, QuerydslConfig.class })
class TrendingChatroomCustomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private TrendingChatroomCustomRepository trendingChatroomCustomRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private TrendingChatroomRepository trendingChatroomRepository;
    @Autowired
    private ChatroomHashTagRepository chatroomHashTagRepository;

    User user1 = User.builder()
        .name("user1-name")
        .position(Position.builder()
            .address("서울시")
            .latitude(36.8)
            .longitude(36.7)
            .build())
        .nickname("user1-nickname")
        .build();

    User user2 = User.builder()
        .name("user2-name")
        .position(Position.builder()
            .address("서울시")
            .latitude(36.8)
            .longitude(36.7)
            .build())
        .nickname("user2-nickname")
        .build();

    User user3 = User.builder()
        .name("user3-name")
        .position(Position.builder()
            .address("서울시")
            .latitude(36.8)
            .longitude(36.7)
            .build())
        .nickname("user3-nickname")
        .build();

    File user1ImageFile = File.builder()
        .fileUrl("user1-image-file-url")
        .fileName(UUID.randomUUID().toString())
        .contentType("user1-image-file-content-type")
        .uploader(user1)
        .byteSize(123L)
        .build();

    File user2ImageFile = File.builder()
        .fileUrl("user2-image-file-url")
        .fileName(UUID.randomUUID().toString())
        .contentType("user2-image-file-content-type")
        .uploader(user2)
        .byteSize(123L)
        .build();

    File user3ImageFile = File.builder()
        .fileUrl("user3-image-file-url")
        .fileName(UUID.randomUUID().toString())
        .contentType("user3-image-file-content-type")
        .uploader(user3)
        .byteSize(123L)
        .build();

    Chatroom chatroom1 = Chatroom.builder()
        .locationLimit(false)
        .searchable(true)
        .name("chatroom1-name")
        .description("chatroom1-description")
        .build();

    Chatroom chatroom2 = Chatroom.builder()
        .locationLimit(false)
        .searchable(true)
        .name("chatroom2-name")
        .description("chatroom2-description")
        .build();

    Chatroom chatroom3 = Chatroom.builder()
        .locationLimit(false)
        .searchable(false)
        .name("chatroom3-name")
        .description("chatroom3-description")
        .build();

    ChatroomParticipant chatroom1Manager = ChatroomParticipant.builder()
        .chatroom(chatroom1)
        .role(ChatroomParticipantRole.MANAGER)
        .user(user1)
        .build();

    ChatroomParticipant chatroom1Participant1 = ChatroomParticipant.builder()
        .chatroom(chatroom1)
        .role(ChatroomParticipantRole.PARTICIPANT)
        .user(user2)
        .build();

    ChatroomParticipant chatroom2Manager = ChatroomParticipant.builder()
        .chatroom(chatroom2)
        .role(ChatroomParticipantRole.MANAGER)
        .user(user2)
        .build();

    TrendingChatroom trendingChatroom1 = TrendingChatroom.builder()
        .chatroom(chatroom1)
        .build();

    TrendingChatroom trendingChatroom2 = TrendingChatroom.builder()
        .chatroom(chatroom2)
        .build();

    TrendingChatroom trendingChatroom3 = TrendingChatroom.builder()
        .chatroom(chatroom3)
        .build();

    ChatroomHashTag chatroom1HashTag;

    ChatroomHashTag chatroom2HashTag;

    ChatroomHashTag chatroom3HashTag;


    @BeforeEach
    void setup() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        user1ImageFile = fileRepository.save(user1ImageFile);
        user2ImageFile = fileRepository.save(user2ImageFile);
        user3ImageFile = fileRepository.save(user3ImageFile);

        user1.updateImage(user1ImageFile);
        user2.updateImage(user2ImageFile);
        user3.updateImage(user3ImageFile);

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        chatroom1 = chatroomRepository.save(chatroom1);
        chatroom2 = chatroomRepository.save(chatroom2);

        chatroom1Manager = chatroomParticipantRepository.save(chatroom1Manager);
        chatroom1Participant1 = chatroomParticipantRepository.save(chatroom1Participant1);
        chatroom2Manager = chatroomParticipantRepository.save(chatroom2Manager);

        chatroom1.updateManager(chatroom1Manager);
        chatroom2.updateManager(chatroom2Manager);

        chatroom1 = chatroomRepository.save(chatroom1);
        chatroom2 = chatroomRepository.save(chatroom2);
        chatroom3 = chatroomRepository.save(chatroom2);

        chatroom1HashTag = chatroomHashTagRepository.save(ChatroomHashTag.builder()
            .chatroom(chatroom1)
            .hashTag("chatroom1-hashTag")
            .build());
        chatroom2HashTag = chatroomHashTagRepository.save(ChatroomHashTag.builder()
            .chatroom(chatroom2)
            .hashTag("chatroom2-hashTag")
            .build());
        chatroom3HashTag = chatroomHashTagRepository.save(ChatroomHashTag.builder()
            .chatroom(chatroom3)
            .hashTag("chatroom3-hashTag")
            .build());

        trendingChatroom1 = trendingChatroomRepository.save(trendingChatroom1);
        trendingChatroom2 = trendingChatroomRepository.save(trendingChatroom2);
    }

    @Nested
    @DisplayName("findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize 메서드는")
    class FindAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize {
        @Test
        @DisplayName("beforeId가 null이면 제외 없이 결과를 반환한다.")
        void findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSizeBeforeIdIsNull() {
            //when
            Slice<TrendingChatroomResponse> result1 = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user1.getUserId(), null, 12);
            Slice<TrendingChatroomResponse> result2 = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 12);

            //then
            assertThat(result1.getContent().size()).isEqualTo(1);
            assertThat(result2.getContent().size()).isEqualTo(2);
        }

        @Test
        @DisplayName("참여하지 않은 채팅방만 검색한다.")
        void findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSizeNotParticipated() {
            //when
            Slice<TrendingChatroomResponse> result = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user2.getUserId(), null, 12);

            //then
            assertThat(result.getContent().size()).isEqualTo(0);
        }

        @Test
        @DisplayName("hasNext로 다음 레코드가 남아있는지 확인 가능한 slice를 반환한다.")
        void findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSizeHasNext() {
            //when
            Slice<TrendingChatroomResponse> result1 = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 1);
            Slice<TrendingChatroomResponse> result2 = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 2);

            //then
            assertThat(result1.getContent().size()).isEqualTo(1);
            assertThat(result1.hasNext()).isTrue();
            assertThat(result2.getContent().size()).isEqualTo(2);
            assertThat(result2.hasNext()).isFalse();
        }

        @Test
        @DisplayName("beforeId 보다 작은 id를 가진 것만 검색한다.")
        void findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSizeBeforeBeforeId() {
            //when
            Slice<TrendingChatroomResponse> result = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), trendingChatroom2.getId(), 2);

            //then
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        @DisplayName("searchable이 true인 것만 검색한다.")
        void findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSizeSearchableTest() {
            //when
            Slice<TrendingChatroomResponse> result = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 2);

            //then
            assertThat(result.getContent().size()).isEqualTo(2);
            assertThat(result.hasNext()).isFalse();
        }

        @Test
        @DisplayName("결과값이 예상한 결과와 동일하다")
        void findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSizeFieldTest() {
            //given
            TrendingChatroomResponse resultExpected = new TrendingChatroomResponse(
                chatroom1.getId(),
                trendingChatroom1.getId(),
                chatroom1.getName(),
                chatroom1.getDescription(),
                List.of(chatroom1HashTag.getHashTag()),
                user1.getNickname(),
                user1ImageFile.getFileUrl(),
                2L
            );

            //when
            Slice<TrendingChatroomResponse> result = trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), trendingChatroom2.getId(), 2);

            //then
            assertThat(result.getContent().get(0)).usingRecursiveComparison().isEqualTo(resultExpected);
        }
    }
}