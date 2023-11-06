package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomHashTag;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomHashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomHashTagCommand {
    private final ChatroomHashTagRepository chatroomHashtagRepository;

    public List<ChatroomHashTag> saveAll(final List<ChatroomHashTag> hashTagList) {
        return chatroomHashtagRepository.saveAll(hashTagList);
    }

    public void deleteByChatroomId(final Long chatroomId) {
        chatroomHashtagRepository.deleteByChatroomId(chatroomId);
    }
}
