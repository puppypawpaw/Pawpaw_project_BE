package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.Getter;

@Getter
public class ChatroomNonParticipantResponse {
    @Schema(description = "유저 아이디", example = "1f739452-5d21-4dc6-b1e6-bd90309b2873")
    private UserId userId;
    @Schema(description = "유저 닉네임", example = "수박이")
    private String nickname;
    @Schema(description = "유저 한줄 소개", example = "4살 뚱이, 6살 마리")
    private String briefIntroduction;
    @Schema(description = "유저 이미지 url", example = "https://example.com")
    private String imageUrl;

    @QueryProjection
    public ChatroomNonParticipantResponse(
        final UserId userId,
        final String nickname,
        final String briefIntroduction,
        final String imageUrl
    ) {
        this.userId = userId;
        this.nickname = nickname;
        this.briefIntroduction = briefIntroduction;
        this.imageUrl = imageUrl;
    }

    public void updateImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
