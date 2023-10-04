package kr.co.pawpaw.dynamodb.domain.chat;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import kr.co.pawpaw.dynamodb.annotation.DynamoDBAutoGeneratedLocalDateTime;
import kr.co.pawpaw.dynamodb.converter.LocalDateTimeConverter;
import kr.co.pawpaw.dynamodb.util.chat.ChatUtil;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "chat")
public class Chat {
    @Id
    private ChatId chatId;

    @Getter @Setter @DynamoDBAttribute
    private String senderId;
    @Getter @Setter @DynamoDBAttribute @DynamoDBTypeConvertedEnum
    private ChatType chatType;
    @Getter @Setter @DynamoDBAttribute
    private String data;
    @Getter @Setter @DynamoDBAttribute @DynamoDBTypeConverted(converter = LocalDateTimeConverter.class) @DynamoDBAutoGeneratedLocalDateTime
    private LocalDateTime createdDate;

    @Builder
    public Chat(
        final Long chatroomId,
        final String senderId,
        final ChatType chatType,
        final String data
    ) {
        setSortId(ChatUtil.createChatSortId());
        setChatroomId(chatroomId);
        this.senderId = senderId;
        this.chatType = chatType;
        this.data = data;
    }

    @DynamoDBHashKey(attributeName = "ChatroomId")
    public Long getChatroomId() {
        return chatId.getChatroomId();
    }

    public void setChatroomId(final Long chatroomId) {
        if (chatId == null) {
            chatId = new ChatId();
        }
        chatId.setChatroomId(chatroomId);
    }

    @DynamoDBRangeKey(attributeName = "SortId")
    public Long getSortId() {
        return chatId.getSortId();
    }

    public void setSortId(final Long sortId) {
        if (chatId == null) {
            chatId = new ChatId();
        }
        chatId.setSortId(sortId);
    }

    public static class ChatId implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long sortId;
        private Long chatroomId;

        @DynamoDBHashKey
        public Long getChatroomId() {
            return chatroomId;
        }

        public void setChatroomId(final Long chatroomId) {
            this.chatroomId = chatroomId;
        }

        @DynamoDBRangeKey
        public Long getSortId() {
            return sortId;
        }

        public void setSortId(final Long sortId) {
            this.sortId = sortId;
        }
    }
}