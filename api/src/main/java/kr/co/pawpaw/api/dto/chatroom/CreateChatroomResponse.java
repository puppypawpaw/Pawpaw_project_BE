package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateChatroomResponse {
    @Schema(description = "채팅방 아이디")
    private final Long chatroomId;

    public static CreateChatroomResponse of(final Chatroom chatroom) {
        return new CreateChatroomResponse(chatroom.getId());
    }
}
