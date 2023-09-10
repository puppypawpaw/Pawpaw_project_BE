package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomCover;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ChatroomCoverRepositoryTest {
    @Autowired
    private ChatroomCoverRepository chatroomCoverRepository;

    @BeforeEach
    void setup() {
        chatroomCoverRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoad() {
        //given
        ChatroomCover chatroomCover = ChatroomCover.builder()
            .chatroom(Chatroom.builder().build())
            .coverFile(File.builder().build())
            .build();

        //when
        chatroomCoverRepository.save(chatroomCover);
        Optional<ChatroomCover> result = chatroomCoverRepository.findById(chatroomCover.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(chatroomCover);
    }
}