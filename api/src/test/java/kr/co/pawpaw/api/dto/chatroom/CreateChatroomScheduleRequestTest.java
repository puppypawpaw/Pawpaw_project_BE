package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CreateChatroomScheduleRequestTest {
    @Test
    @DisplayName("toEntity 메서드는 chatroom과 user 엔티티를 전달받아 chatroomSchedule을 생성한다.")
    void toEntity() {
        //given
        CreateChatroomScheduleRequest request = CreateChatroomScheduleRequest.builder()
            .name("test-name")
            .description("test-description")
            .startDate("2023-09-15 00:11:22")
            .endDate("2023-09-15 11:22:33")
            .build();

        User user = User.builder().build();
        Chatroom chatroom = Chatroom.builder().build();

        ChatroomSchedule resultExpected = ChatroomSchedule.builder()
            .name(request.getName())
            .description(request.getDescription())
            .startDate(LocalDateTime.of(2023, 9, 15, 0, 11, 22))
            .endDate(LocalDateTime.of(2023, 9, 15, 11, 22, 33))
            .creator(user)
            .chatroom(chatroom)
            .build();

        //when
        ChatroomSchedule chatroomSchedule = request.toEntity(chatroom, user);

        //then
        assertThat(chatroomSchedule).usingRecursiveComparison().isEqualTo(resultExpected);
    }
}