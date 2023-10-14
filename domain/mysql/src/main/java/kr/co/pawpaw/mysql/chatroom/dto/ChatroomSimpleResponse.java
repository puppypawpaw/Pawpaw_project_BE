package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.chatroom.domain.Chatroom;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatroomSimpleResponse {
    @Schema(description = "채팅방 이름", example = "천하제일 내 반려동물 자랑방")
    private String name;
    @Schema(description = "채팅방 소개", example = "반려동물을 키우는 사람이라면 누구나 들어와서 자랑해주세요~")
    private String description;
    @Schema(description = "채팅방 참여자 수", example = "2")
    private Long participantNumber;
    @Schema(description = "채팅방 커버 url", example = "https://example.com")
    private String coverUrl;

    @QueryProjection
    public ChatroomSimpleResponse(
        final String name,
        final String description,
        final Long participantNumber,
        final String coverUrl
    ) {
        this.name = name;
        this.description = description;
        this.participantNumber = participantNumber;
        this.coverUrl = coverUrl;
    }
}
