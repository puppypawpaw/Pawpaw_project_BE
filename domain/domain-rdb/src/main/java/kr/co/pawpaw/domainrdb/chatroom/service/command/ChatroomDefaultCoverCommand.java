package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomDefaultCoverRepository;
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
