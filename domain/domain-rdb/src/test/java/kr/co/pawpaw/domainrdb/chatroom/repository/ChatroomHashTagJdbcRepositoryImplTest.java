package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
import kr.co.pawpaw.domainrdb.common.repository.JdbcBatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Import({ChatroomHashTagJdbcRepositoryImpl.class, JdbcBatch.class})
@DataJpaTest
class ChatroomHashTagJdbcRepositoryImplTest {
    @Autowired
    private ChatroomRepository repository;
    @Autowired
    private ChatroomHashTagRepository chatroomHashTagRepository;
    @Autowired
    private ChatroomHashTagJdbcRepository chatroomHashTagJdbcRepository;

    @Test
    @DisplayName("saveAll 및 불러오기 테스트")
    void saveAll() {
        //given
        Chatroom chatroom1 = Chatroom.builder()
            .searchable(true)
            .locationLimit(true)
            .name("chatroom1")
            .build();

        repository.save(chatroom1);

        ChatroomHashTag chatroomHashTag1 = ChatroomHashTag.builder()
            .chatroom(chatroom1)
            .hashTag("ht1")
            .build();

        ChatroomHashTag chatroomHashTag2 = ChatroomHashTag.builder()
            .chatroom(chatroom1)
            .hashTag("ht2")
            .build();

        //when
        List<Long> ids = chatroomHashTagJdbcRepository.saveAll(List.of(chatroomHashTag1, chatroomHashTag2));
        List<ChatroomHashTag> chatroomHashTagList = chatroomHashTagRepository.findAllById(ids);

        //then
        assertThat(chatroomHashTagList.get(0)).usingRecursiveComparison()
            .ignoringFields("id", "modifiedDate", "createdDate")
            .isEqualTo(chatroomHashTag1);
        assertThat(chatroomHashTagList.get(1)).usingRecursiveComparison()
            .ignoringFields("id", "modifiedDate", "createdDate")
            .isEqualTo(chatroomHashTag2);
    }
}