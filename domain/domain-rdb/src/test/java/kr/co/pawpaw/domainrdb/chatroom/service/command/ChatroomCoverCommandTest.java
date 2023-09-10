package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomCover;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomCoverRepository;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomCoverCommandTest {
    @Mock
    private ChatroomCoverRepository chatroomCoverRepository;
    @InjectMocks
    private ChatroomCoverCommand chatroomCoverCommand;

    @Test
    @DisplayName("save 메서드는 chatroomCoverRepository의 save메서드를 호출한다.")
    void save() {
        //given
        Chatroom chatroom = Chatroom.builder().build();
        File file = File.builder().build();

        ChatroomCover chatroomCover = ChatroomCover.builder()
            .chatroom(chatroom)
            .coverFile(file)
            .build();

        //when
        chatroomCoverCommand.save(chatroomCover);

        //then
        verify(chatroomCoverRepository).save(chatroomCover);
    }
}