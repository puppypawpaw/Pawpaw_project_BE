package kr.co.pawpaw.dynamodb.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.dynamodb.chat.domain.Chat;
import kr.co.pawpaw.dynamodb.chat.domain.ChatType;
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
    @Schema(description = "채팅 아이디", example = "1")
    private Long id;
    @Schema(description = "채팅방 아이디", example = "1")
    private Long chatroomId;
    @Schema(description = "채팅 유형", example = "MESSAGE | IMAGE | JOIN | LEAVE")
    private ChatType chatType;
    @Schema(description = "채팅 데이터", example = "안녕하세요!")
    private String data;
    @Schema(description = "채팅 전송자 유저 아이디", example = "1f739452-5d21-4dc6-b1e6-bd90309b2873")
    private String senderId;
    @Schema(description = "채팅 전송자 닉네임", example = "수박이")
    private String sender;
    @Schema(description = "채팅 전송자 이미지 URL", example = "https://example.com")
    private String senderImageUrl;
    @Schema(description = "채팅 생성 시간", example = "2023-09-10 12:00:00")
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
