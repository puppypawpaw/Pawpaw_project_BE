package kr.co.pawpaw.domainrdb.chatroom.service.command;


import kr.co.pawpaw.domainrdb.chatroom.domain.TrendingChatroom;
import kr.co.pawpaw.domainrdb.chatroom.repository.TrendingChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrendingChatroomCommand {
    private final TrendingChatroomRepository trendingChatroomRepository;

    public TrendingChatroom save(final TrendingChatroom trendingChatroom) {
        return trendingChatroomRepository.save(trendingChatroom);
    }
}
