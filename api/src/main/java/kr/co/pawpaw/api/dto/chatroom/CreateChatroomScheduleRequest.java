package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @NotBlank @Schema(description = "스케줄 제목", example = "한강 산책")
    private String name;
    @NotBlank @Schema(description = "스케줄 설명", example = "한강으로 산책갑시다! 배변봉투, 물 등 챙겨오세요~")
    private String description;
    @NotBlank @Schema(description = "스케줄 시작 날짜", example = "2023-09-10 12:00:00")
    private String startDate;
    @NotBlank @Schema(description = "스케줄 종료 날짜", example = "2023-09-11 12:00:00")
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
