package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomQuery {
    private final ChatroomRepository chatroomRepository;

    public Optional<Chatroom> findById(final Long chatroomId) {
        return chatroomRepository.findById(chatroomId);
    }

    public Chatroom getReferenceById(final Long chatroomId) {
        return chatroomRepository.getReferenceById(chatroomId);
    }
}
