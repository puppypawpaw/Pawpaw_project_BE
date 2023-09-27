package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomCoverResponse;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomDefaultCoverCustomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomDefaultCoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomDefaultCoverQuery {
    private final ChatroomDefaultCoverRepository chatroomDefaultCoverRepository;
    private final ChatroomDefaultCoverCustomRepository chatroomDefaultCoverCustomRepository;

    public long count() {
        return chatroomDefaultCoverRepository.count();
    }

    public List<ChatroomCoverResponse> findAllChatroomCover() {
        return chatroomDefaultCoverCustomRepository.findAllChatroomCover();
    }

    public Optional<ChatroomDefaultCover> findById(final Long id) {
        return chatroomDefaultCoverRepository.findById(id);
    }
}
