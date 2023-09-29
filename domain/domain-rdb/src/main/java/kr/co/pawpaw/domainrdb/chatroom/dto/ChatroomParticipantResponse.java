package kr.co.pawpaw.domainrdb.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomParticipantRole;
import lombok.Getter;

@Getter
public class ChatroomParticipantResponse {
    private String nickname;
    private String briefIntroduction;
    private String imageUrl;
    private ChatroomParticipantRole role;

    @QueryProjection
    public ChatroomParticipantResponse(
        final String nickname,
        final String briefIntroduction,
        final String imageUrl,
        final ChatroomParticipantRole role
    ) {
        this.nickname = nickname;
        this.briefIntroduction = briefIntroduction;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public void updateImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
