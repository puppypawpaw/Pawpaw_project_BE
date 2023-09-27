package kr.co.pawpaw.domainrdb.chatroom.repository;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.TrandingChatroom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class TrandingChatroomRepositoryTest {
    @Autowired
    private TrandingChatroomRepository trandingChatroomRepository;
    @Autowired
    private ChatroomRepository chatroomRepository;

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndLoad() {
        //given
        Chatroom chatroom = Chatroom.builder()
            .name("chatroom-name")
            .description("chatroom-description")
            .locationLimit(false)
            .searchable(false)
            .build();

        chatroom = chatroomRepository.save(chatroom);

        TrandingChatroom trandingChatroom = TrandingChatroom.builder()
            .chatroom(chatroom)
            .build();

        //when
        trandingChatroom = trandingChatroomRepository.save(trandingChatroom);
        TrandingChatroom result = trandingChatroomRepository.findById(trandingChatroom.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(trandingChatroom);
    }

    @Nested
    @DisplayName("existsByChatroomId 메소드는")
    class ExistsByChatroomId {
        @Test
        @DisplayName("저장된 뜨고있는 채팅방을 채팅방 아이디 기준으로 존재여부를 반환한다.")
        void checkExist() {
            //given
            Chatroom chatroom = Chatroom.builder()
                .name("chatroom-name")
                .description("chatroom-description")
                .locationLimit(false)
                .searchable(false)
                .build();

            chatroom = chatroomRepository.save(chatroom);

            trandingChatroomRepository.save(TrandingChatroom.builder()
                .chatroom(chatroom)
                .build());

            //when
            boolean result = trandingChatroomRepository.existsByChatroomId(chatroom.getId());

            //then
            assertThat(result).isTrue();
        }
    }
}