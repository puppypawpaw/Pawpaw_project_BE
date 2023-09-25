package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.repository.TrandingChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("TrandingChatroomQuery 클래스의")
@ExtendWith(MockitoExtension.class)
class TrandingChatroomQueryTest {
    @Mock
    private TrandingChatroomRepository trandingChatroomRepository;
    @InjectMocks
    private TrandingChatroomQuery trandingChatroomQuery;

    private Long chatroomId = 12345L;

    @Nested
    @DisplayName("existsByChatroomId 메서드는")
    class ExistsByChatroomId {
        @Test
        @DisplayName("trandingChatroomRepository 의 existsByChatroomId 메서드를 호출한다.")
        void callExistsByChatroomId() {
            //given

            when(trandingChatroomQuery.existsByChatroomId(chatroomId)).thenReturn(true);
            //when
            boolean result = trandingChatroomQuery.existsByChatroomId(chatroomId);

            //then
            assertThat(result).isTrue();
            verify(trandingChatroomRepository).existsByChatroomId(chatroomId);
        }
    }
}