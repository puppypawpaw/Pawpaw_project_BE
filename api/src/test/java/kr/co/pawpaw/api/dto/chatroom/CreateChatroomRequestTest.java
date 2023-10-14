package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.storage.domain.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateChatroomRequestTest {
    private final CreateChatroomRequest request = CreateChatroomRequest.builder()
        .hashTagList(List.of("hashTag1", "hashTag2"))
        .locationLimit(false)
        .searchable(true)
        .name("request name")
        .build();

    @Test
    @DisplayName("toChatroom 메서드는 request의 locationLimit, searchable, name으로 Chatroom만든다.")
    void toChatroom() {
        //given
        //when
        File file = File.builder().build();
        Chatroom chatroom = request.toChatroom(file);

        //then
        assertThat(chatroom).usingRecursiveComparison()
            .ignoringFields("id", "modifiedDate", "createdDate", "hashTagList", "coverFile", "manager", "chatroomParticipants")
            .isEqualTo(request);
    }
}