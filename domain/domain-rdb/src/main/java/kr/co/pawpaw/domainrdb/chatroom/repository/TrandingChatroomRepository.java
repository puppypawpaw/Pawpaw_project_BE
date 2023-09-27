package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.TrandingChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrandingChatroomRepository extends JpaRepository<TrandingChatroom, Long> {
    boolean existsByChatroomId(final Long chatroomId);
}
