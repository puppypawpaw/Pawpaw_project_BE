package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomDetailData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomDetailResponse {
    @Schema(description = "채팅방 아이디")
    private Long id;
    @Schema(description = "채팅방 이름")
    private String name;
    @Schema(description = "채팅방 소개")
    private String description;
    @Schema(description = "채팅방 커버 이미지 url")
    private String coverUrl;
    @Schema(description = "채팅방 최근 대화 시간")
    private String lastChatTime;
    @Schema(description = "채팅방 해시태그 목록")
    private List<String> hashTagList;
    @Schema(description = "채팅방 매니저 이름")
    private String managerName;
    @Schema(description = "채팅방 매니저 이미지 url")
    private String managerImageUrl;
    @Schema(description = "채팅방 참여자 수")
    private Long participantNumber;
    @Schema(description = "채팅방 공지 여부")
    private Boolean hasNotice;
    @Schema(description = "채팅방 스케줄 여부")
    private Boolean hasSchedule;

    public static ChatroomDetailResponse of(final ChatroomDetailData beforeProcessDto) {
        return new ChatroomDetailResponse(
            beforeProcessDto.getId(),
            beforeProcessDto.getName(),
            beforeProcessDto.getDescription(),
            beforeProcessDto.getCoverUrl(),
            TimeUtil.getTimeBeforeString(beforeProcessDto.getLastChatTime()),
            beforeProcessDto.getHashTagList(),
            beforeProcessDto.getManagerName(),
            beforeProcessDto.getManagerImageUrl(),
            beforeProcessDto.getParticipantNumber(),
            beforeProcessDto.getHasNotice(),
            beforeProcessDto.getHasSchedule()
        );
    }
}
