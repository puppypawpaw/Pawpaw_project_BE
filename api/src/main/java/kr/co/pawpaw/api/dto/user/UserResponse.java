package kr.co.pawpaw.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.api.dto.position.PositionResponse;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponse {
    @Schema(description = "유저 이메일", type = "STRING")
    private String email;
    @Schema(description = "유저 역할", type = "STRING")
    private Role role;
    @Schema(description = "유저 닉네임", type = "STRING")
    private String nickname;
    @Schema(description = "유저 위치", type = "STRING")
    private PositionResponse position;


    private UserResponse(
        final String email,
        final Role role,
        final String nickname,
        final PositionResponse position
    ) {
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.position = position;
    }

    public static UserResponse of(final User user) {
        return new UserResponse(
            user.getEmail(),
            user.getRole(),
            user.getNickname(),
            PositionResponse.of(user.getPosition())
        );
    }
}
