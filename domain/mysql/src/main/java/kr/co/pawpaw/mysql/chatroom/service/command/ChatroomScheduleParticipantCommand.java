package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomScheduleParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomScheduleParticipantCommand {
    private final ChatroomScheduleParticipantRepository chatroomScheduleParticipantRepository;

    public ChatroomScheduleParticipant save(final ChatroomScheduleParticipant chatroomScheduleParticipant) {
        return chatroomScheduleParticipantRepository.save(chatroomScheduleParticipant);
    }

    public void delete(final ChatroomScheduleParticipant chatroomScheduleParticipant) {
        chatroomScheduleParticipantRepository.delete(chatroomScheduleParticipant);
    }

    public void deleteByChatroomScheduleId(final Long chatroomScheduleId) {
        chatroomScheduleParticipantRepository.deleteByChatroomScheduleId(chatroomScheduleId);
    }
}
