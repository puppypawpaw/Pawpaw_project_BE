package kr.co.pawpaw.mysql.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.Getter;

@Getter
public class ChatMessageUserDto {
    private final UserId userId;
    private final String nickname;
    private final String imageUrl;

    @QueryProjection
    public ChatMessageUserDto(
        final UserId userId,
        final String nickname,
        final String imageUrl
    ) {
        this.userId = userId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
