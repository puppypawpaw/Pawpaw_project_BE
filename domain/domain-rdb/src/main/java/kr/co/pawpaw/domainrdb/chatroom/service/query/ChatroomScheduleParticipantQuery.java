package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomScheduleParticipantRepository;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomScheduleParticipantQuery {
    private final ChatroomScheduleParticipantRepository chatroomScheduleParticipantRepository;

    public Optional<ChatroomScheduleParticipant> findByChatroomScheduleIdAndUserUserId(
        final Long chatroomScheduleId,
        final UserId userId
    ) {
        return chatroomScheduleParticipantRepository.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId);
    }
}
