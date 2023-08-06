package com.puppy.pawpaw_project_be.webchat.controller;

import com.puppy.pawpaw_project_be.webchat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("chat/enter")
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        sendingOperations.convertAndSend("/pub/chat/room/"+message.getRoomId(),message);
    }

    @MessageMapping("chat/message")
    public void message(ChatMessage message) throws IOException{

    }

}
