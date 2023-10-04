package kr.co.pawpaw.dynamodb.dto.chat;

import kr.co.pawpaw.dynamodb.domain.chat.Chat;
import kr.co.pawpaw.dynamodb.domain.chat.ChatType;
import kr.co.pawpaw.dynamodb.util.chat.ChatUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long chatroomId;
    private ChatType chatType;
    private String data;
    private String senderId;
    private String sender;
    private String senderImageUrl;
    private String createdDate;

    public static ChatMessageDto of(
        final Chat chat,
        final String sender,
        final String senderImageUrl
    ) {
        return ChatMessageDto.builder()
            .id(chat.getSortId())
            .chatroomId(chat.getChatroomId())
            .chatType(chat.getChatType())
            .data(chat.getData())
            .senderId(chat.getSenderId())
            .sender(sender)
            .senderImageUrl(senderImageUrl)
            .createdDate(ChatUtil.localDateTimeToDefaultTimeString(chat.getCreatedDate()))
            .build();
    }
}
