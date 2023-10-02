package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatroomCommandTest {
    @Mock
    private ChatroomRepository chatroomRepository;
    @InjectMocks
    private ChatroomCommand chatroomCommand;

    @Test
    @DisplayName("save메서드는 chatroomRepository의 save메서드를 호출한다.")
    void save() {
        //given
        Chatroom chatroom = Chatroom.builder().build();

        //when
        chatroomCommand.save(chatroom);

        //then
        verify(chatroomRepository).save(chatroom);
    }
}