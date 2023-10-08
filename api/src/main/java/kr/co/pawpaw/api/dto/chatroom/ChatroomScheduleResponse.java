package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomScheduleData;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomScheduleParticipantResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomScheduleResponse {
    @Schema(description = "채팅방 스케줄 아이디", example = "1")
    private Long id;
    @Schema(description = "채팅방 스케줄 이름", example = "한강 산책")
    private String name;
    @Schema(description = "채팅방 스케줄 설명", example = "한강으로 산책갑시다! 배변봉투, 물 등 챙겨오세요~")
    private String description;
    @Schema(description = "채팅장 스케줄 시작 날짜", example = "2023-09-10 12:00:00")
    private String startDate;
    @Schema(description = "채팅장 스케줄 종료 날짜", example = "2023-09-11 12:00:00")
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
