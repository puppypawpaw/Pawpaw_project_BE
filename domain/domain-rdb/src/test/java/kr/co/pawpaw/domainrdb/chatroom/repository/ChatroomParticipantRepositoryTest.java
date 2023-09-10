package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        Chatroom chatroom = Chatroom.builder()
            .build();

        chatroomRepository.save(chatroom);

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