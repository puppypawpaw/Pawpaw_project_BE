package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomScheduleCommand {
    private final ChatroomScheduleRepository chatroomScheduleRepository;

    public ChatroomSchedule save(final ChatroomSchedule chatroomSchedule) {
        return chatroomScheduleRepository.save(chatroomSchedule);
    }

    public void deleteById(final Long chatroomId) {
        chatroomScheduleRepository.deleteById(chatroomId);
    }
}
