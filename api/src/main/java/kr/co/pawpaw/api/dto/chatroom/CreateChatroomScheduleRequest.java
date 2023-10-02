package kr.co.pawpaw.api.dto.chatroom;

import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomSchedule;
import kr.co.pawpaw.mysql.user.domain.User;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateChatroomScheduleRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String startDate;
    @NotBlank
    private String endDate;

    public ChatroomSchedule toEntity(
        final Chatroom chatroom,
        final User creator
    ) {
        return ChatroomSchedule.builder()
            .name(name)
            .description(description)
            .startDate(TimeUtil.defaultTimeStringToLocalDateTime(startDate))
            .endDate(TimeUtil.defaultTimeStringToLocalDateTime(endDate))
            .chatroom(chatroom)
            .creator(creator)
            .build();
    }
}
