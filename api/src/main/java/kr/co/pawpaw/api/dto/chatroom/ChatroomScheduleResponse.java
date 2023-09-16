package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomScheduleParticipantResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomScheduleResponse {
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private Collection<ChatroomScheduleParticipantResponse> participantList;

    public static ChatroomScheduleResponse of(
        final ChatroomScheduleData beforeProcessData
    ) {
        return new ChatroomScheduleResponse(
            beforeProcessData.getId(),
            beforeProcessData.getName(),
            beforeProcessData.getDescription(),
            TimeUtil.localDateTimeToDefaultTimeString(beforeProcessData.getStartDate()),
            TimeUtil.localDateTimeToDefaultTimeString(beforeProcessData.getEndDate()),
            beforeProcessData.getParticipants()
        );
    }
}
