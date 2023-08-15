package kr.co.pawpaw.domainrdb.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponse {
    @Schema(description = "유저 아이디", type = "STRING")
    private String id;
    @Schema(description = "유저 역할", type = "STRING")
    private Role role;
    @Schema(description = "유저 닉네임", type = "STRING")
    private String nickname;
    @Schema(description = "유저 전화번호", type = "STRING")
    private String phoneNumber;

    private UserResponse(
        final String id,
        final Role role,
        final String nickname,
        final String phoneNumber
    ) {
        this.id = id;
        this.role = role;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public static UserResponse of(final User user) {
        return new UserResponse(
            user.getId(),
            user.getRole(),
            user.getNickname(),
            user.getPhoneNumber()
        );
    }
}
