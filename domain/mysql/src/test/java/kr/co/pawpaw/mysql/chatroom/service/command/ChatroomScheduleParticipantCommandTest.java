package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomScheduleParticipantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatroomScheduleParticipantCommandTest {
    @Mock
    private ChatroomScheduleParticipantRepository chatroomScheduleParticipantRepository;
    @InjectMocks
    private ChatroomScheduleParticipantCommand chatroomScheduleParticipantCommand;

    @Test
    @DisplayName("save 메서드는 chatroomScheduleParticipantRepository의 save메서드를 호출하고 save 메서드의 반환값을 그대로 호출한다.")
    void save() {
        //given
        ChatroomScheduleParticipant chatroomScheduleParticipant = ChatroomScheduleParticipant.builder().build();
        when(chatroomScheduleParticipantRepository.save(chatroomScheduleParticipant)).thenReturn(chatroomScheduleParticipant);

        //when
        ChatroomScheduleParticipant result = chatroomScheduleParticipantCommand.save(chatroomScheduleParticipant);

        //then
        verify(chatroomScheduleParticipantRepository).save(chatroomScheduleParticipant);
        assertThat(result).isEqualTo(chatroomScheduleParticipant);
    }

    @Test
    @DisplayName("delete 메서드는 chatroomScheduleParticipantRepository의 delete 메서드를 호출한다.")
    void delete() {
        //given
        ChatroomScheduleParticipant chatroomScheduleParticipant = ChatroomScheduleParticipant.builder().build();

        //when
        chatroomScheduleParticipantCommand.delete(chatroomScheduleParticipant);

        //then
        verify(chatroomScheduleParticipantRepository).delete(chatroomScheduleParticipant);
    }
}