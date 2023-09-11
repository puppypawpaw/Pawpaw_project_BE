package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateChatroomResponse {
    private final Long chatroomId;

    public static CreateChatroomResponse of(final Chatroom chatroom) {
        return new CreateChatroomResponse(chatroom.getId());
    }
}
