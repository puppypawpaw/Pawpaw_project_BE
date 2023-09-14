package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.domainrdb.chatroom.domain.TrandingChatroom;
import kr.co.pawpaw.domainrdb.chatroom.dto.TrandingChatroomResponse;
import kr.co.pawpaw.domainrdb.config.QuerydslConfig;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.repository.FileRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { TrandingChatroomCustomRepository.class, QuerydslConfig.class })
@DataJpaTest
class TrandingChatroomCustomRepositoryTest {
    @Autowired
    private TrandingChatroomCustomRepository trandingChatroomCustomRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private TrandingChatroomRepository trandingChatroomRepository;

    User user1 = User.builder()
        .name("user1-name")
        .nickname("user1-nickname")
        .build();

    User user2 = User.builder()
        .name("user2-name")
        .nickname("user2-nickname")
        .build();

    User user3 = User.builder()
        .name("user3-name")
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
        .hashTagList(List.of("hashTag1", "hashTag2"))
        .build();

    Chatroom chatroom2 = Chatroom.builder()
        .locationLimit(false)
        .searchable(true)
        .name("chatroom2-name")
        .description("chatroom2-description")
        .hashTagList(List.of("hashTag1", "hashTag2"))
        .build();

    Chatroom chatroom3 = Chatroom.builder()
        .locationLimit(false)
        .searchable(false)
        .name("chatroom3-name")
        .description("chatroom3-description")
        .hashTagList(List.of("hashTag1", "hashTag2"))
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

    TrandingChatroom trandingChatroom1 = TrandingChatroom.builder()
        .chatroom(chatroom1)
        .build();

    TrandingChatroom trandingChatroom2 = TrandingChatroom.builder()
        .chatroom(chatroom2)
        .build();

    TrandingChatroom trandingChatroom3 = TrandingChatroom.builder()
        .chatroom(chatroom3)
        .build();


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

        trandingChatroom1 = trandingChatroomRepository.save(trandingChatroom1);
        trandingChatroom2 = trandingChatroomRepository.save(trandingChatroom2);
    }

    @Test
    @DisplayName("findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize 메서드는 beforeId 가 null이면 제외 없이 결과를 반환한다.")
    void findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSizeBeforeIdIsNull() {
        //given

        //when
        Slice<TrandingChatroomResponse> result1 = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user1.getUserId(), null, 12);
        Slice<TrandingChatroomResponse> result2 = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 12);

        //then
        assertThat(result1.getContent().size()).isEqualTo(1);
        assertThat(result2.getContent().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize 메서드는 참여하지 않은 채팅방만 검색한다.")
    void findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSizeNotParticipated() {
        //given

        //when
        Slice<TrandingChatroomResponse> result = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user2.getUserId(), null, 12);

        //then
        assertThat(result.getContent().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize 메서드는 hashNext로 다음 entity가 존재하는지 파악 가능하다.")
    void findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSizeHasNext() {
        //given

        //when
        Slice<TrandingChatroomResponse> result1 = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 1);
        Slice<TrandingChatroomResponse> result2 = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 2);

        //then
        assertThat(result1.getContent().size()).isEqualTo(1);
        assertThat(result1.hasNext()).isTrue();
        assertThat(result2.getContent().size()).isEqualTo(2);
        assertThat(result2.hasNext()).isFalse();
    }

    @Test
    @DisplayName("findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize 메서드는 beforeId 미만의 id를 가진 뜨고있는 채팅방만 검색한다.")
    void findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSizeBeforeBeforeId() {
        //given

        //when
        Slice<TrandingChatroomResponse> result = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), trandingChatroom2.getId(), 2);

        //then
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize 메서드는 searchable이 true인 chatroom만 검색한다.")
    void findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSizeSearchableTest() {
        //given

        //when
        Slice<TrandingChatroomResponse> result = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), null, 2);

        //then
        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize 메서드는 필드 값 테스트")
    void findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSizeFieldTest() {
        //given
        TrandingChatroomResponse resultExpected = new TrandingChatroomResponse(
            chatroom1.getId(),
            trandingChatroom1.getId(),
            chatroom1.getName(),
            chatroom1.getDescription(),
            chatroom1.getHashTagList(),
            user1.getNickname(),
            user1ImageFile.getFileUrl(),
            2L
        );

        //when
        Slice<TrandingChatroomResponse> result = trandingChatroomCustomRepository.findAccessibleTrandingChatroomByUserIdAndBeforeIdAndSize(user3.getUserId(), trandingChatroom2.getId(), 2);

        //then
        assertThat(result.getContent().get(0)).usingRecursiveComparison().isEqualTo(resultExpected);
    }
}