package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chat;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatType;
import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatRepositoryTest {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Test
    @DisplayName("chat 저장 및 불러오기")
    void saveAndLoad() {
        //given
        Chatroom chatroom = Chatroom.builder()
            .name("name")
            .description("chatroom description")
            .searchable(false)
            .locationLimit(false)
            .build();

        chatroom = chatroomRepository.save(chatroom);

        Chat chat = Chat.builder()
            .chatroom(chatroom)
            .data("chat data")
            .type(ChatType.MESSAGE)
            .build();

        //when
        chat = chatRepository.save(chat);

        Chat result = chatRepository.findById(chat.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(result).usingRecursiveComparison()
            .isEqualTo(chat);
    }
}