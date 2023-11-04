package kr.co.pawpaw.mysql.chatroom.repository;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.TrendingChatroom;
import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TrendingChatroomRepository 는")
class TrendingChatroomRepositoryTest extends MySQLTestContainer {
    @Autowired
    private TrendingChatroomRepository trendingChatroomRepository;
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

        TrendingChatroom trendingChatroom = TrendingChatroom.builder()
            .chatroom(chatroom)
            .build();

        //when
        trendingChatroom = trendingChatroomRepository.save(trendingChatroom);
        TrendingChatroom result = trendingChatroomRepository.findById(trendingChatroom.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(trendingChatroom);
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

            trendingChatroomRepository.save(TrendingChatroom.builder()
                .chatroom(chatroom)
                .build());

            //when
            boolean result = trendingChatroomRepository.existsByChatroomId(chatroom.getId());

            //then
            assertThat(result).isTrue();
        }
    }
}