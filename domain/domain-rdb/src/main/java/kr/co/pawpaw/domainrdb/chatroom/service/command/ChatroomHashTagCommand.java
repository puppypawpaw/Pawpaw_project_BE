package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomHashTagJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomHashTagCommand {
    private final ChatroomHashTagJdbcRepository chatroomHashTagJdbcRepository;

    public List<Long> saveAll(final List<ChatroomHashTag> chatroomHashTagList) {
        return chatroomHashTagJdbcRepository.saveAll(chatroomHashTagList);
    }
}
