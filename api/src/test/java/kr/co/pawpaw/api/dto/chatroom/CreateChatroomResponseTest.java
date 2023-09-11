package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class CreateChatroomResponseTest {
    @Test
    @DisplayName("of 메서드는 chatroom entity의 id를 사용해서 만들어진다.")
    void of() throws NoSuchFieldException, IllegalAccessException {
        //given
        Chatroom chatroom = Chatroom.builder()
            .build();
        Long id = 123L;
        Field idField = Chatroom.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(chatroom, id);

        //when
        CreateChatroomResponse response = CreateChatroomResponse.of(chatroom);

        //then
        assertThat(response.getChatroomId()).isEqualTo(id);
    }
}