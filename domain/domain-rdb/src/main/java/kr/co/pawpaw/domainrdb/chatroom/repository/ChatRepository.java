package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
