package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomScheduleRepository extends JpaRepository<ChatroomSchedule, Long> {
    boolean existsByChatroomIdAndId(
        final Long chatroomId,
        final Long chatroomScheduleId
    );
}
