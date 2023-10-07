package kr.co.pawpaw.ws.controller.chat;

import kr.co.pawpaw.ws.dto.chat.SendChatMessageRequest;
import kr.co.pawpaw.ws.service.chat.ChatService;
import kr.co.pawpaw.ws.util.auth.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@MessageMapping("/chatroom/{chatroomId}")
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/message")
    public void sendMessage(
        @Payload final SendChatMessageRequest request,
        @DestinationVariable("chatroomId") final Long chatroomId,
        final SimpMessageHeaderAccessor headerAccessor
    ) {
        chatService.sendMessage(request, chatroomId, AuthUtil.getUserIdFromSimpMessageHeaderAccessor(headerAccessor));
    }
}
