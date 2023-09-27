package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomDetailData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ChatroomDetailResponseTest {
    @Test
    @DisplayName("of 메서드는 ChatroomDetailData dto의 LocalDateTime field를 string field로 변환한다.")
    void of() {
        //given
        ChatroomDetailData beforeProcessDto = new ChatroomDetailData(
            1L,
            "name",
            "description",
            "coverUrl",
            LocalDateTime.now(),
            List.of("hashTag1", "hashTag2"),
            "managerName",
            "managerImageUrl",
            123L,
            false,
            false
        );

        //when
        ChatroomDetailResponse result = ChatroomDetailResponse.of(beforeProcessDto);

        //then
        assertThat(result).usingRecursiveComparison()
            .ignoringFields("lastChatTime")
            .isEqualTo(beforeProcessDto);

        assertThat(result.getLastChatTime()).isEqualTo("방금");
    }
}