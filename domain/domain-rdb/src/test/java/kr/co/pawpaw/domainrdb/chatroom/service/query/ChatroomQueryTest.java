package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatroomQueryTest {
    @Mock
    private ChatroomRepository chatroomRepository;
    @InjectMocks
    private ChatroomQuery chatroomQuery;

    @Test
    @DisplayName("findById메서드는 chatroomRepository의 findById메서드를 호출한다.")
    void findById() {
        //given
        Long chatroomId = 123L;

        //when
        chatroomQuery.findById(chatroomId);

        //then
        verify(chatroomRepository).findById(chatroomId);
    }

    @Test
    @DisplayName("getReferenceById는 chatroomRepository의 getReferenceById메서드를 호출한다.")
    void getReferenceById() {
        //given
        Long chatroomId = 123L;
        //when
        chatroomQuery.getReferenceById(chatroomId);

        //then
        verify(chatroomRepository).getReferenceById(chatroomId);
    }
}