package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomCustomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.TrendingChatroomCustomRepository;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChatroomQueryTest {
    @Mock
    private ChatroomRepository chatroomRepository;
    @Mock
    private TrendingChatroomCustomRepository trendingChatroomCustomRepository;
    @Mock
    private ChatroomCustomRepository chatroomCustomRepository;
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

    @Test
    @DisplayName("getParticipatedChatroomDetailDataByUserId 메서드는 chatroomCustomRepository의 findAllByUserIdWithDetailData메서드를 호출한다.")
    void getParticipatedChatroomDetailDataByUserId() {
        //given
        UserId userId = UserId.create();

        when(chatroomCustomRepository.findAllByUserIdWithDetailData(userId)).thenReturn(List.of());

        //when
        List<?> result = chatroomQuery.getParticipatedChatroomDetailDataByUserId(userId);

        //then
        verify(chatroomCustomRepository).findAllByUserIdWithDetailData(userId);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("getAccessibleNewChatroomByUserId 메서드는 chatroomCustomRepository의 findAccessibleNewChatroomByUserId메서드를 호출한다.")
    void getAccessibleNewChatroomByUserId() {
        //given
        UserId userId = UserId.create();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdDate"));

        when(chatroomCustomRepository.findAccessibleNewChatroomByUserId(userId)).thenReturn(List.of());
        //when
        List<?> result = chatroomQuery.getAccessibleNewChatroomByUserId(userId);

        //then
        verify(chatroomCustomRepository).findAccessibleNewChatroomByUserId(userId);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("getAccessibleTrendingChatroom 메서드는 trendingChatroomCustomRepository 의 findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize 메서드를 호출한다.")
    void getAccessibleTrendingChatroom() {
        //given
        UserId userId = UserId.create();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdDate"));

        when(trendingChatroomCustomRepository.findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(userId, null, 10)).thenReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), false));
        //when
        Slice<?> result = chatroomQuery.getAccessibleTrendingChatroom(userId, null, 10);

        //then
        verify(trendingChatroomCustomRepository).findAccessibleTrendingChatroomByUserIdAndBeforeIdAndSize(userId, null, 10);
        assertThat(result.getNumber()).isEqualTo(0);
    }
}