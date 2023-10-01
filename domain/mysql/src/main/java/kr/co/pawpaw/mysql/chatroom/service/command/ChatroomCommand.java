package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomCommand {
    private final ChatroomRepository chatroomRepository;

    public Chatroom save(final Chatroom chatroom) {
        return chatroomRepository.save(chatroom);
    }
}
