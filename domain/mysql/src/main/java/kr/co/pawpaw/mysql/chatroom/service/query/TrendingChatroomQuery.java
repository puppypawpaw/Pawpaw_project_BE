package kr.co.pawpaw.mysql.chatroom.service.query;

import kr.co.pawpaw.mysql.chatroom.repository.TrendingChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrendingChatroomQuery {
    private final TrendingChatroomRepository trendingChatroomRepository;

    public boolean existsByChatroomId(final Long chatroomId) {
        return trendingChatroomRepository.existsByChatroomId(chatroomId);
    }
}
