package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

class CreateChatroomScheduleResponseTest {
    @Test
    @DisplayName("of 메서드는 ChatroomSchedule의 id로 CreateChatroomScheduleResponse 를 생성함")
    void of() throws NoSuchFieldException, IllegalAccessException {
        //given
        ChatroomSchedule chatroomSchedule = ChatroomSchedule.builder().build();
        Field idField = chatroomSchedule.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(chatroomSchedule, 123L);

        //when
        CreateChatroomScheduleResponse response = CreateChatroomScheduleResponse.of(chatroomSchedule);

        //then
        assertThat(response.getChatroomScheduleId()).isEqualTo(chatroomSchedule.getId());
    }
}