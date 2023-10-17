package kr.co.pawpaw.ws.dto.chat;

import kr.co.pawpaw.dynamodb.chat.domain.Chat;
import kr.co.pawpaw.dynamodb.chat.domain.ChatType;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendChatMessageRequest {
    private String data;

    public Chat toEntity(
        final Long chatroomId,
        final UserId userId
    ) {
        return Chat.builder()
            .chatroomId(chatroomId)
            .senderId(userId.getValue())
            .chatType(ChatType.MESSAGE)
            .data(data)
            .build();
    }
}
