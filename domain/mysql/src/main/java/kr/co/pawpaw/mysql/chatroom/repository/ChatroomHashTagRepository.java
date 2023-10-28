package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomHashTagRepository extends JpaRepository<ChatroomHashTag, Long> {
}
