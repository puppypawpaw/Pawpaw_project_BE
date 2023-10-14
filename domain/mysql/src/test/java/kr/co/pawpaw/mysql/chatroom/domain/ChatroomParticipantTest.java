package kr.co.pawpaw.mysql.chatroom.domain;

import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChatroomParticipantTest {

    @Test
    @DisplayName("isManager는 ChatroomParticipantRole.MANAGER가 role인지 여부를 의미한다.")
    void isManager() {
        //given
        ChatroomParticipant participant1 = ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.MANAGER)
            .chatroom(Chatroom.builder().build())
            .user(User.builder().build())
            .build();

        ChatroomParticipant participant2 = ChatroomParticipant.builder()
            .role(ChatroomParticipantRole.PARTICIPANT)
            .chatroom(Chatroom.builder().build())
            .user(User.builder().build())
            .build();

        //when
        boolean result1 = participant1.isManager();
        boolean result2 = participant2.isManager();

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}