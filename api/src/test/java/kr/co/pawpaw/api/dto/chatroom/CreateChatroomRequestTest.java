package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
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
        Chatroom chatroom = request.toChatroom();

        //then
        assertThat(chatroom).usingRecursiveComparison()
            .ignoringFields("id", "modifiedDate", "createdDate")
            .isEqualTo(request);
    }

    @Test
    @DisplayName("toChatroomHashTags 메서드는 request의 hashTagList와 chatroom을 사용해서 chatroomHashTagList를 만든다.")
    void toChatroomHashTags() {
        //given
        Chatroom chatroom = Chatroom.builder()
            .build();

        //when
        List<ChatroomHashTag> chatroomHashTagList = request.toChatroomHashTags(chatroom);

        //then
        assertThat(chatroomHashTagList.size()).isEqualTo(request.getHashTagList().size());
        assertThat(chatroomHashTagList.stream()
            .filter(chatroomHashTag -> chatroomHashTag.getChatroom().equals(chatroom))
            .count()).isEqualTo(request.getHashTagList().size());
        assertThat(chatroomHashTagList.stream()
            .map(ChatroomHashTag::getHashTag)
            .filter(hashTag -> request.getHashTagList().contains(hashTag))
            .count()).isEqualTo(request.getHashTagList().size());
    }
}