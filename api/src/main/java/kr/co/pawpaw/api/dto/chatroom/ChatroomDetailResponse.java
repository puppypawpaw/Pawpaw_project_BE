package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.util.time.TimeUtil;
import kr.co.pawpaw.mysql.chatroom.dto.ChatroomDetailData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatroomDetailResponse {
    @Schema(description = "채팅방 아이디", example = "1")
    private Long id;
    @Schema(description = "채팅방 이름", example = "천하제일 내 반려동물 자랑방")
    private String name;
    @Schema(description = "채팅방 소개", example = "반려동물을 키우는 사람이라면 누구나 들어와서 자랑해주세요~")
    private String description;
    @Schema(description = "채팅방 커버 이미지 url", example = "https://example.com")
    private String coverUrl;
    @Schema(description = "채팅방 최근 대화 시간", example = "방금 | 1분전 | 1일전")
    private String lastChatTime;
    @Schema(description = "채팅방 해시태그 목록", example = "['강아지', '고양이', '20대 이상']")
    private List<String> hashTagList;
    @Schema(description = "채팅방 매니저 이름", example = "지상최강감자")
    private String managerName;
    @Schema(description = "채팅방 매니저 이미지 url", example = "https://example.com")
    private String managerImageUrl;
    @Schema(description = "채팅방 참여자 수", example = "2")
    private Long participantNumber;
    @Schema(description = "채팅방 공지 여부", example = "true | false")
    private Boolean hasNotice;
    @Schema(description = "채팅방 스케줄 여부", example = "true | false")
    private Boolean hasSchedule;

    public static ChatroomDetailResponse of(
        final ChatroomDetailData beforeProcessDto,
        final LocalDateTime lastChatTime
    ) {
        return new ChatroomDetailResponse(
            beforeProcessDto.getId(),
            beforeProcessDto.getName(),
            beforeProcessDto.getDescription(),
            beforeProcessDto.getCoverUrl(),
            TimeUtil.getTimeBeforeString(lastChatTime),
            beforeProcessDto.getHashTagList(),
            beforeProcessDto.getManagerName(),
            beforeProcessDto.getManagerImageUrl(),
            beforeProcessDto.getParticipantNumber(),
            beforeProcessDto.getHasNotice(),
            beforeProcessDto.getHasSchedule()
        );
    }
}
