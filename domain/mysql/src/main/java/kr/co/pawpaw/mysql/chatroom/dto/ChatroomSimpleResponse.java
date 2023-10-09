package kr.co.pawpaw.mysql.chatroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomSimpleResponse {
    @Schema(description = "채팅방 아이디", example = "1")
    private Long id;
    @Schema(description = "채팅방 이름", example = "천하제일 내 반려동물 자랑방")
    private String name;
    @Schema(description = "채팅방 소개", example = "반려동물을 키우는 사람이라면 누구나 들어와서 자랑해주세요~")
    private String description;
    @Schema(description = "채팅방 해시태그 목록", example = "['강아지', '고양이', '20대 이상']")
    private List<String> hashTagList;

    public static ChatroomSimpleResponse of(final Chatroom chatroom) {
        return new ChatroomSimpleResponse(
            chatroom.getId(),
            chatroom.getName(),
            chatroom.getDescription(),
            chatroom.getHashTagList()
        );
    }
}
