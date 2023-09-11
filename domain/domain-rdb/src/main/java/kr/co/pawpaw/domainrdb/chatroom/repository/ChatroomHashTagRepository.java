package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomHashTagRepository extends JpaRepository<ChatroomHashTag, Long> {
}
