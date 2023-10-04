package kr.co.pawpaw.dynamodb.repository;

import kr.co.pawpaw.dynamodb.common.AbstractDynamodbIntegrationTest;
import kr.co.pawpaw.dynamodb.domain.chat.Chat;
import kr.co.pawpaw.dynamodb.domain.chat.ChatType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ChatRepository 클래스의")
class ChatRepositoryTest extends AbstractDynamodbIntegrationTest {
    @Autowired
    private ChatRepository chatRepository;

    @Nested
    @DisplayName("save 메서드는")
    class save {
        Chat chat = Chat.builder()
            .chatroomId(123L)
            .chatType(ChatType.MESSAGE)
            .senderId("senderId")
            .data("message")
            .build();

        @BeforeEach
        void setup() {
            chatRepository.deleteAll();
        }

        @Test
        @DisplayName("Chat을 repository에 저장한다.")
        void saveChatInRepository() {
            chat = chatRepository.save(chat);

            //when
            Optional<Chat> savedChat = chatRepository.findByChatroomIdAndSortId(chat.getChatroomId(), chat.getSortId());

            //then
            assertThat(savedChat.isPresent()).isTrue();
            assertThat(savedChat.get()).usingRecursiveComparison().isEqualTo(chat);
        }
    }
}