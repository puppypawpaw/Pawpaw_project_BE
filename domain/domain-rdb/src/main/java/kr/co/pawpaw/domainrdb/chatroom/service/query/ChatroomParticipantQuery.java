package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomParticipantResponse;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomParticipantCustomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomParticipantRepository;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomParticipantQuery {
    private final ChatroomParticipantRepository chatroomParticipantRepository;
    private final ChatroomParticipantCustomRepository chatroomParticipantCustomRepository;

    public Optional<ChatroomParticipant> findByUserIdAndChatroomId(
        final UserId userId,
        final Long chatroomId
    ) {
        return chatroomParticipantRepository.findByUserUserIdAndChatroomId(userId, chatroomId);
    }

    public boolean existsByUserIdAndChatroomId(
        final UserId userId,
        final Long chatroomId
    ) {
        return chatroomParticipantRepository.existsByUserUserIdAndChatroomId(userId, chatroomId);
    }

    public List<ChatroomParticipantResponse> getChatroomParticipantResponseList(final Long chatroomId) {
        return chatroomParticipantCustomRepository.getChatroomParticipantResponseList(chatroomId);
    }
}
