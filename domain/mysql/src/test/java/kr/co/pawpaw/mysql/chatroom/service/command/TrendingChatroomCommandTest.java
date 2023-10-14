package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.TrendingChatroom;
import kr.co.pawpaw.mysql.chatroom.repository.TrendingChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrendingChatroomCommandTest {
    @Mock
    private TrendingChatroomRepository trendingChatroomRepository;
    @InjectMocks
    private TrendingChatroomCommand trendingChatroomCommand;

    @Test
    @DisplayName("save 메서드는 trendingChatroomCommand의 save메서드를 호출한다.")
    void save() {
        //given
        TrendingChatroom trendingChatroom = TrendingChatroom.builder().build();

        when(trendingChatroomRepository.save(trendingChatroom)).thenReturn(trendingChatroom);

        //when
        trendingChatroomCommand.save(trendingChatroom);

        //then
        verify(trendingChatroomRepository).save(trendingChatroom);
    }
}