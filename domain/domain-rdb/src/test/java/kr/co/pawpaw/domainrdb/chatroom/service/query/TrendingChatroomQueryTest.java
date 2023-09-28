package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.repository.TrendingChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TrendingChatroomQuery 클래스의")
@ExtendWith(MockitoExtension.class)
class TrendingChatroomQueryTest {
    @Mock
    private TrendingChatroomRepository trendingChatroomRepository;
    @InjectMocks
    private TrendingChatroomQuery trendingChatroomQuery;

    private Long chatroomId = 12345L;

    @Nested
    @DisplayName("existsByChatroomId 메서드는")
    class ExistsByChatroomId {
        @Test
        @DisplayName("trendingChatroomRepository 의 existsByChatroomId 메서드를 호출한다.")
        void callExistsByChatroomId() {
            //given

            when(trendingChatroomQuery.existsByChatroomId(chatroomId)).thenReturn(true);
            //when
            boolean result = trendingChatroomQuery.existsByChatroomId(chatroomId);

            //then
            assertThat(result).isTrue();
            verify(trendingChatroomRepository).existsByChatroomId(chatroomId);
        }
    }
}