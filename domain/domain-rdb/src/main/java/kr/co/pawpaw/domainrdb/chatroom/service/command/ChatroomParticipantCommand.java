package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomParticipantCommand {
    private final ChatroomParticipantRepository chatroomParticipantRepository;

    public ChatroomParticipant save(final ChatroomParticipant chatroomParticipant) {
        return chatroomParticipantRepository.save(chatroomParticipant);
    }

    public void delete(final ChatroomParticipant chatroomParticipant) {
        chatroomParticipantRepository.delete(chatroomParticipant);
    }
}
