package kr.co.pawpaw.ws.service.chat;

import kr.co.pawpaw.dynamodb.domain.chat.Chat;
import kr.co.pawpaw.dynamodb.dto.chat.ChatMessageDto;
import kr.co.pawpaw.dynamodb.service.chat.command.ChatCommand;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.dto.ChatMessageUserDto;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import kr.co.pawpaw.redis.service.pub.RedisPublisher;
import kr.co.pawpaw.ws.dto.chat.SendChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatCommand chatCommand;
    private final RedisPublisher redisPublisher;
    private final ChannelTopic chatTopic;
    private final UserQuery userQuery;

//    @Transactional
    public void sendMessage(
        final SendChatMessageRequest request,
        final Long chatroomId,
        final UserId userId
    ) {
        Chat sendChat = chatCommand.save(request.toEntity(chatroomId, userId));

        ChatMessageUserDto userDto = userQuery.getChatMessageUserDtoByUserId(userId);

        redisPublisher.publish(
            chatTopic,
            ChatMessageDto.of(
                sendChat,
                userDto.getNickname(),
                userDto.getImageUrl()
            )
        );
    }
}
