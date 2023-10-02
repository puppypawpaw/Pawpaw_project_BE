package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomDefaultCoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomDefaultCoverCommand {
    private final ChatroomDefaultCoverRepository chatroomDefaultCoverRepository;

    public List<ChatroomDefaultCover> saveAll(final Iterable<ChatroomDefaultCover> chatroomDefaultCovers) {
        return chatroomDefaultCoverRepository.saveAll(chatroomDefaultCovers);
    }
}
