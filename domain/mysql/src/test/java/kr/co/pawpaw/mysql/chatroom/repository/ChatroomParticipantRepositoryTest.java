package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatroomParticipantRepository 는")
class ChatroomParticipantRepositoryTest extends MySQLTestContainer {
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private UserRepository userRepository;

    LocalDateTime now = LocalDateTime.now();
    User uploader;
    Chatroom chatroom;
    User user;
    ChatroomParticipant chatroomParticipant;
    @BeforeEach
    void setup() {
        uploader = userRepository.save(User.builder()
            .email("tnt7986@email.com")
            .name("user")
            .nickname("nickname")
            .password("password")
            .createdDate(now)
            .modifiedDate(now)
            .phoneNumber("phoneNumber")
            .position(Position.builder()
                .latitude(12.3)
                .longitude(13.2)
                .address("position")
                .build())
            .build());

        chatroom = chatroomRepository.save(Chatroom.builder()
            .description("description")
            .locationLimit(false)
            .searchable(false)
            .name("name")
            .build());

        user = userRepository.save(User.builder()
                .position(Position.builder()
                    .address("서울시 강동구")
                    .latitude(36.8)
                    .longitude(36.6)
                    .build())
            .build());

        chatroomParticipant = chatroomParticipantRepository.save(ChatroomParticipant.builder()
            .chatroom(chatroom)
            .role(ChatroomParticipantRole.PARTICIPANT)
            .user(user)
            .build());
    }

    @Test
    @DisplayName("저장 및 findByUserUserIdAndChatroomId 메서드 테스트")
    void findByUserUserIdAndChatroomId() {
        //when
        Optional<ChatroomParticipant> result = chatroomParticipantRepository.findByUserUserIdAndChatroomId(user.getUserId(), chatroom.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(chatroomParticipant);
    }

    @Test
    @DisplayName("저장 및 existsByUserUserIdAndChatroomId 메서드 반환값 테스트")
    void existsByUserUserIdAndChatroomId() {
        //given
        Long chatroomId = chatroom.getId();
        Long notExistsChatroomId2 = 1234L;
        UserId notExistsUserId = UserId.create();

        //when
        boolean result1 = chatroomParticipantRepository.existsByUserUserIdAndChatroomId(user.getUserId(), chatroomId);
        boolean result2 = chatroomParticipantRepository.existsByUserUserIdAndChatroomId(user.getUserId(), notExistsChatroomId2);
        boolean result3 = chatroomParticipantRepository.existsByUserUserIdAndChatroomId(notExistsUserId, chatroomId);
        boolean result4 = chatroomParticipantRepository.existsByUserUserIdAndChatroomId(notExistsUserId, notExistsChatroomId2);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }
}