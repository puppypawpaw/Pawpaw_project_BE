package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomParticipantRepository extends JpaRepository<ChatroomParticipant, Long> {
    Optional<ChatroomParticipant> findByUserUserIdAndChatroomId(
        final UserId userId,
        final Long chatroomId
    );

    boolean existsByUserUserIdAndChatroomId(
        final UserId userId,
        final Long chatroomId
    );
}
