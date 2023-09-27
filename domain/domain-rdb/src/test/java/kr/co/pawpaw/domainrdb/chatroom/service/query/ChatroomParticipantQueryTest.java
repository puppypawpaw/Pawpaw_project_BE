package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomParticipantRepository;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatroomParticipantQueryTest {
    @Mock
    private ChatroomParticipantRepository chatroomParticipantRepository;
    @InjectMocks
    private ChatroomParticipantQuery chatroomParticipantQuery;

    @Test
    @DisplayName("findByUserIdAndChatroomId메서드는 chatroomParticipantRepository의 findByUserUserIdAndChatroomId를 호출한다.")
    void findByUserIdAndChatroomId() {
        //given
        UserId userId = UserId.create();
        Long roomId = 123L;

        //when
        chatroomParticipantQuery.findByUserIdAndChatroomId(userId, roomId);

        //then
        verify(chatroomParticipantRepository).findByUserUserIdAndChatroomId(userId, roomId);
    }

    @Test
    @DisplayName("existsByUserIdAndChatroomId 메서드는 chatroomParticipantRepository 의 existsByUserUserIdAndChatroomId 메서드의 반환값을 반환한다.")
    void existsByUserIdAndChatroomId() {
        //given
        UserId userId = UserId.create();
        Long roomId = 123L;
        Long roomId2 = 1234L;
        when(chatroomParticipantRepository.existsByUserUserIdAndChatroomId(userId, roomId)).thenReturn(true);
        when(chatroomParticipantRepository.existsByUserUserIdAndChatroomId(userId, roomId2)).thenReturn(false);

        //when
        boolean result1 = chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, roomId);
        boolean result2 = chatroomParticipantQuery.existsByUserIdAndChatroomId(userId, roomId2);

        //then
        verify(chatroomParticipantRepository).existsByUserUserIdAndChatroomId(userId, roomId);
        verify(chatroomParticipantRepository).existsByUserUserIdAndChatroomId(userId, roomId2);
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}