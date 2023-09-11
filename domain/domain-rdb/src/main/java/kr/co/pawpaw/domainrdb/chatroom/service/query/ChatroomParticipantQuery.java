package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomParticipantRepository;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomParticipantQuery {
    private final ChatroomParticipantRepository chatroomParticipantRepository;

    public Optional<ChatroomParticipant> findByUserIdAndChatroomId(
        final UserId userId,
        final Long chatroomId
    ) {
        return chatroomParticipantRepository.findByUserUserIdAndChatroomId(userId, chatroomId);
    }
}
