package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InviteChatroomUserRequest {
    @NotNull
    @Schema(description = "초대할 유저 아이디", example = "1f739452-5d21-4dc6-b1e6-bd90309b2873")
    private UserId userId;
}
