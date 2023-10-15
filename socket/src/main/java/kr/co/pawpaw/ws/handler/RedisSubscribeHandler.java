package kr.co.pawpaw.ws.handler;

import kr.co.pawpaw.dynamodb.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscribeHandler {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void handleDto(final Object rawDto) {
        Class<?> clazz = rawDto.getClass();

        if (clazz.equals(ChatMessageDto.class)) {
            ChatMessageDto dto = (ChatMessageDto) rawDto;

            simpMessagingTemplate.convertAndSend(getChatMessageDestination(dto), dto);
        }
    }

    private String getChatMessageDestination(final ChatMessageDto dto) {
        return String.format("/sub/chatroom/%d/message", dto.getChatroomId());
    }
}
