package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomScheduleParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomScheduleParticipantCommand {
    private final ChatroomScheduleParticipantRepository chatroomScheduleParticipantRepository;

    public ChatroomScheduleParticipant save(final ChatroomScheduleParticipant chatroomScheduleParticipant) {
        return chatroomScheduleParticipantRepository.save(chatroomScheduleParticipant);
    }
}
