package kr.co.pawpaw.domainrdb.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ChatroomNonParticipantResponse {
    private String nickname;
    private String briefIntroduction;
    private String imageUrl;

    @QueryProjection
    public ChatroomNonParticipantResponse(
        final String nickname,
        final String briefIntroduction,
        final String imageUrl
    ) {
        this.nickname = nickname;
        this.briefIntroduction = briefIntroduction;
        this.imageUrl = imageUrl;
    }

    public void updateImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
