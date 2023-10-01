package kr.co.pawpaw.mysql.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ChatroomCoverResponse {
    private Long id;
    private String coverUrl;

    @QueryProjection
    public ChatroomCoverResponse(final Long id, final String coverUrl) {
        this.id = id;
        this.coverUrl = coverUrl;
    }
}
