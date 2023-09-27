package kr.co.pawpaw.domainrdb.chatroom.service.command;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomScheduleRepository;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatroomScheduleCommandTest {
    @Mock
    private ChatroomScheduleRepository chatroomScheduleRepository;
    @InjectMocks
    private ChatroomScheduleCommand chatroomScheduleCommand;

    @Test
    @DisplayName("save 메서드는 ChatroomSchedule ChatroomSchedulerepository의 save메서드를 호출한다.")
    void save() {
        //given
        ChatroomSchedule chatroomSchedule = ChatroomSchedule.builder()
            .creator(User.builder().build())
            .name("scheduleName")
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now())
            .build();

        when(chatroomScheduleRepository.save(chatroomSchedule)).thenReturn(chatroomSchedule);

        //when
        chatroomScheduleCommand.save(chatroomSchedule);

        //then
        verify(chatroomScheduleRepository).save(chatroomSchedule);
    }
}