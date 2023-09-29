package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.TrendingChatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendingChatroomRepository extends JpaRepository<TrendingChatroom, Long> {
    boolean existsByChatroomId(final Long chatroomId);
}
