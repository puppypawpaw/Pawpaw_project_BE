package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.repository.TrandingChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrandingChatroomQuery {
    private final TrandingChatroomRepository trandingChatroomRepository;

    public boolean existsByChatroomId(final Long chatroomId) {
        return trandingChatroomRepository.existsByChatroomId(chatroomId);
    }
}
