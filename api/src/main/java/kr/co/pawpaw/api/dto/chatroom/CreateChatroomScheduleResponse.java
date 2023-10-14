package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateChatroomScheduleResponse {
    private Long chatroomScheduleId;

    public static CreateChatroomScheduleResponse of(final ChatroomSchedule chatroomSchedule) {
        return new CreateChatroomScheduleResponse(chatroomSchedule.getId());
    }
}
