package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomScheduleParticipantRepository extends JpaRepository<ChatroomScheduleParticipant, Long> {
}
