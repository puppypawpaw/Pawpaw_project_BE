package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ChatroomScheduleParticipantRepositoryTest {
    @Autowired
    private ChatroomScheduleParticipantRepository chatroomScheduleParticipantRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomScheduleRepository chatroomScheduleRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;

    User user;
    Chatroom chatroom;
    ChatroomSchedule chatroomSchedule;
    ChatroomScheduleParticipant chatroomScheduleParticipant;

    @BeforeEach
    void setup() {
        user = userRepository.save(User.builder().build());
        chatroom = chatroomRepository.save(Chatroom.builder()
            .name("chatroom-name")
            .description("chatroom-description")
            .locationLimit(false)
            .searchable(true)
            .build());
        chatroomSchedule = chatroomScheduleRepository.save(ChatroomSchedule.builder()
            .name("chatroomSchedule-name")
            .description("chatroomSchedule-descrition")
            .startDate(LocalDateTime.of(2023,9,16,15,13,0))
            .endDate(LocalDateTime.of(2023,9,17,15,13,0))
            .chatroom(chatroom)
            .creator(user)
            .build());
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoad() {
        //given

        //when
        chatroomScheduleParticipant = chatroomScheduleParticipantRepository.save(ChatroomScheduleParticipant.builder()
            .chatroomSchedule(chatroomSchedule)
            .user(user)
            .build());

        Optional<ChatroomScheduleParticipant> result = chatroomScheduleParticipantRepository.findById(chatroomScheduleParticipant.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(chatroomScheduleParticipant);
    }
}