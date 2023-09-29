package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomDetailData;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomResponse;
import kr.co.pawpaw.domainrdb.chatroom.dto.TrendingChatroomResponse;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomCustomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.TrendingChatroomCustomRepository;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomQuery {
    private final ChatroomRepository chatroomRepository;
    private final ChatroomCustomRepository chatroomCustomRepository;
    private final TrendingChatroomCustomRepository trendingChatroomCustomRepository;

    public Optional<Chatroom> findById(final Long chatroomId) {
        return chatroomRepository.findById(chatroomId);
    }

    public Chatroom getReferenceById(final Long chatroomId) {
        return chatroomRepository.getReferenceById(chatroomId);
    }

    public List<ChatroomDetailData> getParticipatedChatroomDetailDataByUserId(final UserId userId) {
        return chatroomCustomRepository.findAllByUserIdWithDetailData(userId);
    }

    public List<ChatroomResponse> getAccessibleNewChatroomByUserId(final UserId userId) {
        return chatroomCustomRepository.findAccessibleNewChatroomByUserId(userId);
    }

    public Slice<TrendingChatroomResponse> getAccessibleTrendingChatroom(
        final UserId userId,
        final Long beforeId,
        final int size
    ) {
        return trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(userId, beforeId, size);
    }
}
