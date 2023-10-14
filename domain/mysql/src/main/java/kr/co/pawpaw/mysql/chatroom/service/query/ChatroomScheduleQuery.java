package kr.co.pawpaw.mysql.chatroom.service.query;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomScheduleCustomRepository;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomScheduleQuery {
    private final ChatroomScheduleRepository chatroomScheduleRepository;
    private final ChatroomScheduleCustomRepository chatroomScheduleCustomRepository;

    public List<ChatroomScheduleData> findNotEndChatroomScheduleByChatroomId(
        final Long chatroomId
    ) {
        return chatroomScheduleCustomRepository.findNotEndChatroomScheduleByChatroomId(chatroomId);
    }

    public ChatroomSchedule getReferenceById(final Long chatroomScheduleId) {
        return chatroomScheduleRepository.getReferenceById(chatroomScheduleId);
    }

    public boolean existByChatroomIdAndChatroomScheduleId(
        final Long chatroomId,
        final Long chatroomScheduleId
    ) {
        return chatroomScheduleRepository.existsByChatroomIdAndId(chatroomId, chatroomScheduleId);
    }
}
