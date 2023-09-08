package kr.co.pawpaw.ws.controller.test;

import kr.co.pawpaw.domainredis.chatMessage.vo.ChatMessageVO;
import kr.co.pawpaw.ws.application.TestApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TestController {
    private final TestApplication testApplication;

    @MessageMapping("/chat/message")
    public void message(ChatMessageVO message) {
        if (ChatMessageVO.MessageType.JOIN.equals(message.getType()))
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

    }
}
