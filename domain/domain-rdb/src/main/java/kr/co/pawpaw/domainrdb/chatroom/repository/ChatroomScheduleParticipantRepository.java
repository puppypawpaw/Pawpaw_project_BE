package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomScheduleParticipantRepository extends JpaRepository<ChatroomScheduleParticipant, Long> {
    Optional<ChatroomScheduleParticipant> findByChatroomScheduleIdAndUserUserId(final Long chatroomScheduleId, final UserId userId);
}
