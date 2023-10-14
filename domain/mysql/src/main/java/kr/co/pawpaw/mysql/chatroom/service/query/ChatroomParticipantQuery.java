package kr.co.pawpaw.mysql.chatroom.service.query;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomParticipantResponse;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomParticipantCustomRepository;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomParticipantRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
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

    public List<ChatroomParticipant> findAllByChatroomId(final Long chatroomId) {
        return chatroomParticipantRepository.findAllByChatroomId(chatroomId);
    }
}
