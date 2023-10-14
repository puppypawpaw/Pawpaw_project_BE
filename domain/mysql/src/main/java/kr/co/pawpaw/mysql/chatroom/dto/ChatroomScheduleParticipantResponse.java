package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChatroomScheduleParticipantResponse {
    @Schema(description = "채팅장 스케줄 참여자 닉네임", example = "수박이")
    private String nickname;
    @Schema(description = "채팅장 스케줄 참여자 이미지 url", example = "https://example.com")
    private String imageUrl;

    @QueryProjection
    public ChatroomScheduleParticipantResponse(
        final String nickname,
        final String imageUrl
    ) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public void updateImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
