package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomParticipantRepository;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

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
}