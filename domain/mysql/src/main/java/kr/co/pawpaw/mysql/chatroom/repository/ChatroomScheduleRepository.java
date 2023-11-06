package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomScheduleRepository extends JpaRepository<ChatroomSchedule, Long> {
    boolean existsByChatroomIdAndId(
        final Long chatroomId,
        final Long chatroomScheduleId
    );

    void deleteByChatroomId(final Long chatroomId);
}
