package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.Chatroom;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipant;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomParticipantRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomParticipantCommandTest {
    @Mock
    private ChatroomParticipantRepository chatroomParticipantRepository;

    @InjectMocks
    private ChatroomParticipantCommand chatroomParticipantCommand;

    Chatroom chatroom = Chatroom.builder().build();
    User user = User.builder().build();
    ChatroomParticipant chatroomParticipant = ChatroomParticipant.builder()
        .chatroom(chatroom)
        .role(ChatroomParticipantRole.PARTICIPANT)
        .user(user)
        .build();

    @Test
    @DisplayName("save 메서드는 chatroomParticipantRepository의 save메서드를 호출한다.")
    void save() {
        //given
        //when
        chatroomParticipantCommand.save(chatroomParticipant);

        //then
        verify(chatroomParticipantRepository).save(chatroomParticipant);
    }

    @Test
    @DisplayName("delete 메서드는 chatroomParticipantRepository의 delete메서드를 호출한다.")
    void delete() {
        //given
        //when
        chatroomParticipantCommand.delete(chatroomParticipant);

        //then
        verify(chatroomParticipantRepository).delete(chatroomParticipant);
    }
}