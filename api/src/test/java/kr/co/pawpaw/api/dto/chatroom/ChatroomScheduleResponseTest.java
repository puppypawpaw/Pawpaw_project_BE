package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomScheduleData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ChatroomScheduleResponseTest {
    @Test
    @DisplayName("of 메서드는 ChatroomScheduleData의 localdateTime 필드를 string field로 변환하여 ChatroomScheduleResponse를 생성한다.")
    void of() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        ChatroomScheduleData before = new ChatroomScheduleData(
            "name",
            "description",
            start,
            end,
            List.of()
        );

        Constructor<ChatroomScheduleResponse> constructor = ChatroomScheduleResponse.class.getDeclaredConstructor(
            String.class,
            String.class,
            String.class,
            String.class,
            List.class
        );

        constructor.setAccessible(true);

        ChatroomScheduleResponse resultExpected = constructor.newInstance(
            before.getName(),
            before.getDescription(),
            TimeUtil.localDateTimeToDefaultTimeString(start),
            TimeUtil.localDateTimeToDefaultTimeString(end),
            before.getParticipants()
        );

        //when
        ChatroomScheduleResponse response = ChatroomScheduleResponse.of(before);

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(resultExpected);
    }
}