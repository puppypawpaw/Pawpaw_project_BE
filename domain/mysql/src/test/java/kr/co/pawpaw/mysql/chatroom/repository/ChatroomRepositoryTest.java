package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatroomRepository는")
class ChatroomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoadTest() {
        //given
        Chatroom chatroom = Chatroom.builder()
            .locationLimit(true)
            .description("chatroom description")
            .name("chatroomName")
            .searchable(true)
            .build();

        //when
        chatroomRepository.save(chatroom);
        Optional<Chatroom> result = chatroomRepository.findById(chatroom.getId());

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(chatroom);
    }
}