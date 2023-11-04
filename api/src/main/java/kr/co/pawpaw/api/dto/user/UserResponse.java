package kr.co.pawpaw.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.dto.position.PositionResponse;
import kr.co.pawpaw.mysql.user.domain.Role;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.Getter;

@Getter
public class UserResponse {
    @Schema(description = "유저 아이디", type = "STRING", example = "5abdf94f-f813-4511-b0ac-78832a05d16a")
    private UserId userId;
    @Schema(description = "유저 이메일", type = "STRING", example = "abc@gmail.com")
    private String email;
    @Schema(description = "유저 역할", type = "STRING", example = "ADMIN | USER")
    private Role role;
    @Schema(description = "유저 닉네임", type = "STRING", example = "수박이")
    private String nickname;
    @Schema(description = "유저 한줄 소개", type="STRING", example="3살 강쥐 수박이, 2살 앵무새 메론")
    private String briefIntroduction;
    @Schema(description = "유저 위치")
    private PositionResponse position;
    @Schema(description = "유저 이미지 URL", type = "STRING", example = "https://example.com")
    private String imageUrl;

    private UserResponse(
        final UserId userId,
        final String email,
        final Role role,
        final String nickname,
        final String briefIntroduction,
        final PositionResponse position,
        final String imageUrl
    ) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.briefIntroduction = briefIntroduction;
        this.position = position;
        this.imageUrl = imageUrl;
    }

    public static UserResponse of(
        final User user,
        final String imageUrl
    ) {
        return new UserResponse(
            user.getUserId(),
            user.getEmail(),
            user.getRole(),
            user.getNickname(),
            user.getBriefIntroduction(),
            PositionResponse.of(user.getPosition()),
            imageUrl
        );
    }
}
