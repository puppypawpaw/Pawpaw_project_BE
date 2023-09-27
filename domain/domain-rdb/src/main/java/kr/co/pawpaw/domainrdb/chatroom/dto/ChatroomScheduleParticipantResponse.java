package kr.co.pawpaw.domainrdb.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ChatroomScheduleParticipantResponse {
    private String nickname;
    private String imageUrl;

    @QueryProjection
    public ChatroomScheduleParticipantResponse(
        final String nickname,
        final String imageUrl
    ) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
