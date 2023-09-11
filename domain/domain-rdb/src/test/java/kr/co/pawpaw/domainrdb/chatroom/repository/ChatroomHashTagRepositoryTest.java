package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ChatroomHashTagRepositoryTest {
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private ChatroomHashTagRepository chatroomHashTagRepository;

    @Test
    @DisplayName("저장 및 불러오기")
    void saveAndLoad() {
        //given
        Chatroom chatroom = Chatroom.builder().build();
        chatroomRepository.save(chatroom);

        ChatroomHashTag chatroomHashTag = ChatroomHashTag.builder()
            .hashTag("hashTagContent")
            .chatroom(chatroom)
            .build();

        //when
        chatroomHashTagRepository.save(chatroomHashTag);
        Optional<ChatroomHashTag> result = chatroomHashTagRepository.findById(chatroomHashTag.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(chatroomHashTag);
    }
}