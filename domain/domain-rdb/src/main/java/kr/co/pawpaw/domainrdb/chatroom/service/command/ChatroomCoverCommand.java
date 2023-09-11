package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomCover;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomCoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomCoverCommand {
    private final ChatroomCoverRepository chatroomCoverRepository;

    public ChatroomCover save(final ChatroomCover chatroomCover) {
        return chatroomCoverRepository.save(chatroomCover);
    }
}
