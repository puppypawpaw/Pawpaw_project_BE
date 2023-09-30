package kr.co.pawpaw.domainrdb.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.Getter;

@Getter
public class ChatroomNonParticipantResponse {
    private UserId userId;
    private String nickname;
    private String briefIntroduction;
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
