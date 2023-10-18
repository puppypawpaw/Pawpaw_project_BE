package kr.co.pawpaw.mysql.chatroom.service.command;


import kr.co.pawpaw.mysql.chatroom.domain.TrendingChatroom;
import kr.co.pawpaw.mysql.chatroom.repository.TrendingChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrendingChatroomCommand {
    private final TrendingChatroomRepository trendingChatroomRepository;

    public TrendingChatroom save(final TrendingChatroom trendingChatroom) {
        return trendingChatroomRepository.save(trendingChatroom);
    }

    public void deleteByChatroomId(final Long chatroomId) {
        trendingChatroomRepository.deleteByChatroomId(chatroomId);
    }
}
