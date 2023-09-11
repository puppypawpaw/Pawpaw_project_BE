package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;

import java.util.List;

public interface ChatroomHashTagJdbcRepository {
    List<Long> saveAll(final List<ChatroomHashTag> chatroomHashTagCollection);
}
