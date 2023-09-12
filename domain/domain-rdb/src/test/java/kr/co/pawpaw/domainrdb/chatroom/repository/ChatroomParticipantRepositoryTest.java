package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.domainrdb.position.Position;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatroomParticipantRepositoryTest {
    @Autowired
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("저장 및 findByUserUserIdAndChatroomId 메서드 테스트")
    void findByUserUserIdAndChatroomId() {
        //given
        LocalDateTime now = LocalDateTime.now();

        User uploader = User.builder()
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
                .name("position")
                .build())
            .build();

        uploader = userRepository.save(uploader);


        Chatroom chatroom = Chatroom.builder()
            .description("description")
            .hashTagList(List.of("hashTag1", "hashTag2"))
            .locationLimit(false)
            .searchable(false)
            .name("name")
            .build();

        chatroom = chatroomRepository.save(chatroom);

        User user = User.builder().build();

        userRepository.save(user);

        ChatroomParticipant chatroomParticipant = ChatroomParticipant.builder()
            .chatroom(chatroom)
            .role(ChatroomParticipantRole.PARTICIPANT)
            .user(user)
            .build();

        //when
        chatroomParticipantRepository.save(chatroomParticipant);
        Optional<ChatroomParticipant> result = chatroomParticipantRepository.findByUserUserIdAndChatroomId(user.getUserId(), chatroom.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(chatroomParticipant);
    }
}