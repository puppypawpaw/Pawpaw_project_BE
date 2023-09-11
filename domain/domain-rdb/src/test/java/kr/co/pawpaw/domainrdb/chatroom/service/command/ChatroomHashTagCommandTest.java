package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomHashTag;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomHashTagJdbcRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomHashTagCommandTest {
    @Mock
    private ChatroomHashTagJdbcRepository chatroomHashTagJdbcRepository;
    @InjectMocks
    private ChatroomHashTagCommand chatroomHashTagCommand;

    @Test
    @DisplayName("saveAll 메서드는 chatroomHashTagJdbcRepository의 saveAll 메서드를 호출한다.")
    void saveAll() {
        //given
        List<ChatroomHashTag> chatroomHashTagList = List.of(
            ChatroomHashTag.builder().build(),
            ChatroomHashTag.builder().build()
        );

        //when
        chatroomHashTagCommand.saveAll(chatroomHashTagList);

        //then
        verify(chatroomHashTagJdbcRepository).saveAll(chatroomHashTagList);
    }
}