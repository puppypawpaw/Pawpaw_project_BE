package kr.co.pawpaw.mysql.chatroom.service.query;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomScheduleParticipant;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomScheduleParticipantRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatroomScheduleParticipantQueryTest {
    @Mock
    private ChatroomScheduleParticipantRepository chatroomScheduleParticipantRepository;
    @InjectMocks
    private ChatroomScheduleParticipantQuery chatroomScheduleParticipantQuery;

    @Test
    @DisplayName("findByChatroomScheduleIdAndUserUserId 메서드는 chatroomScheduleParticipantRepository의 findByChatroomScheduleIdAndUserUserId 메서드를 호출하고 반환값을 그대로 반환한다.")
    void findByChatroomScheduleIdAndUserUserId() {
        //given
        Long chatroomScheduleId = 123L;
        UserId userId = UserId.create();

        ChatroomScheduleParticipant chatroomScheduleParticipant = ChatroomScheduleParticipant.builder().build();
        when(chatroomScheduleParticipantRepository.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId)).thenReturn(Optional.of(chatroomScheduleParticipant));

        //when
        Optional<ChatroomScheduleParticipant> result = chatroomScheduleParticipantQuery.findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId);

        //then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(chatroomScheduleParticipant);
        verify(chatroomScheduleParticipantRepository).findByChatroomScheduleIdAndUserUserId(chatroomScheduleId, userId);
    }
}