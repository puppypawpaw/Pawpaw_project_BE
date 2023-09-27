package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
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
class ChatroomScheduleRepositoryTest {
    @Autowired
    private ChatroomScheduleRepository chatroomScheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;

    private User user = User.builder().build();
    private Chatroom chatroom = Chatroom.builder()
        .name("chatroom-name")
        .description("chatroom-description")
        .searchable(true)
        .locationLimit(true)
        .build();
    private ChatroomSchedule chatroomSchedule = ChatroomSchedule.builder()
        .name("name")
        .description("description")
        .startDate(LocalDateTime.of(2023,9,16,14,8,0))
        .endDate(LocalDateTime.of(2023,9,17,14,8,0))
        .creator(user)
        .chatroom(chatroom)
        .build();

    @BeforeEach
    void setup() {
        user = userRepository.save(user);
        chatroom = chatroomRepository.save(chatroom);
        chatroomSchedule = chatroomScheduleRepository.save(chatroomSchedule);
    }

    @Test
    @DisplayName("저장 및 existsByChatroomIdAndId 반환값 테스트")
    void existsByChatroomIdAndId() {
        //given
        Long chatroomId = chatroom.getId();
        Long notExistsChatroomId = -12345L;
        Long chatroomScheduleId = chatroomSchedule.getId();
        Long notExistsChatroomScheduleId = -54321L;

        //when
        boolean result1 = chatroomScheduleRepository.existsByChatroomIdAndId(chatroomId, chatroomScheduleId);
        boolean result2 = chatroomScheduleRepository.existsByChatroomIdAndId(notExistsChatroomId, chatroomScheduleId);
        boolean result3 = chatroomScheduleRepository.existsByChatroomIdAndId(chatroomId, notExistsChatroomScheduleId);
        boolean result4 = chatroomScheduleRepository.existsByChatroomIdAndId(notExistsChatroomId, notExistsChatroomScheduleId);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }
}