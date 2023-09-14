package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.TrandingChatroom;
import kr.co.pawpaw.domainrdb.chatroom.repository.TrandingChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrandingChatroomCommandTest {
    @Mock
    private TrandingChatroomRepository trandingChatroomRepository;
    @InjectMocks
    private TrandingChatroomCommand trandingChatroomCommand;

    @Test
    @DisplayName("save 메서드는 trandingChatroomCommand의 save메서드를 호출한다.")
    void save() {
        //given
        TrandingChatroom trandingChatroom = TrandingChatroom.builder().build();

        when(trandingChatroomRepository.save(trandingChatroom)).thenReturn(trandingChatroom);

        //when
        trandingChatroomCommand.save(trandingChatroom);

        //then
        verify(trandingChatroomRepository).save(trandingChatroom);
    }
}