package kr.co.pawpaw.ws.service.chat;

import kr.co.pawpaw.dynamodb.chat.domain.Chat;
import kr.co.pawpaw.dynamodb.chat.dto.ChatMessageDto;
import kr.co.pawpaw.dynamodb.chat.service.command.ChatCommand;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.mysql.user.dto.ChatMessageUserDto;
import kr.co.pawpaw.mysql.user.service.query.UserQuery;
import kr.co.pawpaw.redis.service.pub.RedisPublisher;
import kr.co.pawpaw.ws.dto.chat.SendChatMessageRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.listener.ChannelTopic;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("ChatService 의")
@ExtendWith(MockitoExtension.class)
class ChatServiceTest {
    @Mock
    private ChatCommand chatCommand;
    @Mock
    private RedisPublisher redisPublisher;
    @Mock
    private ChannelTopic chatTopic;
    @Mock
    private UserQuery userQuery;
    @InjectMocks
    private ChatService chatService;

    @Nested
    @DisplayName("sendMessage 메서드는")
    class SendMessage {
        SendChatMessageRequest request = SendChatMessageRequest.builder()
            .data("메시지")
            .build();
        UserId userId = UserId.create();
        Long chatroomId = 123L;
        ChatMessageUserDto dto = new ChatMessageUserDto(userId, "nickname", "imageUrl");
        Chat savedChat = request.toEntity(chatroomId, userId);
        ChatMessageDto messageDto = ChatMessageDto.of(savedChat, dto.getNickname(), dto.getImageUrl());

        @Test
        @DisplayName("chatCommand의 save메서드와 userQuery의 getChatMessageUserDtoByUserId, redisPublisher의 publish를 호출한다.")
        void callChatCommandSave() {
            //given
            when(chatCommand.save(any(Chat.class))).thenReturn(savedChat);
            when(userQuery.getChatMessageUserDtoByUserId(userId)).thenReturn(dto);

            //when
            chatService.sendMessage(request, chatroomId, userId);

            //then
            verify(chatCommand).save(any(Chat.class));
            verify(userQuery).getChatMessageUserDtoByUserId(userId);
            ArgumentCaptor<ChatMessageDto> argumentCaptor = ArgumentCaptor.forClass(ChatMessageDto.class);
            verify(redisPublisher).publish(eq(chatTopic), argumentCaptor.capture());
            assertThat(argumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(messageDto);
        }
    }
}