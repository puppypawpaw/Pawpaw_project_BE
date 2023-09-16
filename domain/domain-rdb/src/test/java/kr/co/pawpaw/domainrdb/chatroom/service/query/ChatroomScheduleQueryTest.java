package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomScheduleCustomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChatroomScheduleQueryTest {
    @Mock
    private ChatroomScheduleRepository chatroomScheduleRepository;
    @Mock
    private ChatroomScheduleCustomRepository chatroomScheduleCustomRepository;
    @InjectMocks
    private ChatroomScheduleQuery chatroomScheduleQuery;

    @Test
    @DisplayName("findNotEndChatroomScheduleByChatroomId 메서드는 chatroomScheduleCustomRepository 의 findNotEndChatroomScheduleByChatroomId 메서드의 반환값을 그대로 반환한다..")
    void findNotEndChatroomScheduleByChatroomId() {
        //given
        Long chatroomId = 123L;
        List<ChatroomScheduleData> scheduleDataList = List.of();
        when(chatroomScheduleCustomRepository.findNotEndChatroomScheduleByChatroomId(chatroomId)).thenReturn(scheduleDataList);

        //when
        List<ChatroomScheduleData> result = chatroomScheduleQuery.findNotEndChatroomScheduleByChatroomId(chatroomId);

        //then
        assertThat(result).isEqualTo(scheduleDataList);
    }

    @Test
    @DisplayName("getReferenceById 메서드는 chatroomScheduleRepository가 반환하는 결과를 그대로 반환한다.")
    void getReferenceById() {
        //given
        Long chatroomScheduleId = 1234L;
        ChatroomSchedule chatroomSchedule = ChatroomSchedule.builder().build();

        when(chatroomScheduleRepository.getReferenceById(chatroomScheduleId)).thenReturn(chatroomSchedule);

        //when
        ChatroomSchedule result = chatroomScheduleQuery.getReferenceById(chatroomScheduleId);

        //then
        assertThat(result).isEqualTo(chatroomSchedule);
    }

    @Test
    @DisplayName("existByChatroomIdAndChatroomScheduleId 메서드는 chatroomScheduleRepository 의 existsByChatroomIdAndId 메서드 호출값을 그대로 반환한다.")
    void existByChatroomIdAndChatroomScheduleId() {
        //given
        Long chatroomId = 123L;
        Long chatroomScheduleId = 1234L;
        Long chatroomScheduleId2 = 12345L;

        when(chatroomScheduleRepository.existsByChatroomIdAndId(chatroomId, chatroomScheduleId)).thenReturn(true);
        when(chatroomScheduleRepository.existsByChatroomIdAndId(chatroomId, chatroomScheduleId2)).thenReturn(false);

        //when
        boolean result1 = chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId2);
        boolean result2 = chatroomScheduleQuery.existByChatroomIdAndChatroomScheduleId(chatroomId, chatroomScheduleId);

        //then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }
}