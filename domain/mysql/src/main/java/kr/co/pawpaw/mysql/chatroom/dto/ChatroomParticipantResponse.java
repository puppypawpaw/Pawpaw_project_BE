package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomParticipantRole;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.Getter;

@Getter
public class ChatroomParticipantResponse {
    @Schema(description = "채팅방 참여자 유저아이디", example = "9764f0e7-68dd-464c-b218-2e99a33fa634")
    private UserId userId;
    @Schema(description = "채팅방 참여자 닉네임", example = "수박이")
    private String nickname;
    @Schema(description = "채팅방 참여자 한줄소개", example = "4살 뭉이, 6살 감자")
    private String briefIntroduction;
    @Schema(description = "채팅방 참여자 이미지 url", example = "https://example.com")
    private String imageUrl;
    @Schema(description = "채팅방 참여자 역할", example = "MANAGER | PARTICIPANT")
    private ChatroomParticipantRole role;

    @QueryProjection
    public ChatroomParticipantResponse(
        final UserId userId,
        final String nickname,
        final String briefIntroduction,
        final String imageUrl,
        final ChatroomParticipantRole role
    ) {
        this.userId = userId;
        this.nickname = nickname;
        this.briefIntroduction = briefIntroduction;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public void updateImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
