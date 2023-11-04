package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatroomScheduleRepository 는")
class ChatroomScheduleRepositoryTest extends MySQLTestContainer {
    @Autowired
    private ChatroomScheduleRepository chatroomScheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;

    private User user = User.builder()
        .position(Position.builder()
            .address("서울특별시 강동구")
            .latitude(36.8)
            .longitude(36.7)
            .build())
        .build();
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