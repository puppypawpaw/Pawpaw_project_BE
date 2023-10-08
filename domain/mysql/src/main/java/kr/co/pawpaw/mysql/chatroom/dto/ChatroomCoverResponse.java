package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ChatroomCoverResponse {
    @Schema(description = "채팅방 기본 커버 아이디", example = "1")
    private Long id;
    @Schema(description = "채팅방 기본 커버 url", example = "https://example.com")
    private String coverUrl;

    @QueryProjection
    public ChatroomCoverResponse(final Long id, final String coverUrl) {
        this.id = id;
        this.coverUrl = coverUrl;
    }
}
