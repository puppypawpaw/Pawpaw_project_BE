package kr.co.pawpaw.api.dto.chatroom;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateChatroomManagerRequest {
    @Schema(description = "위임 대상 유저 아이디", example = "1f739452-5d21-4dc6-b1e6-bd90309b2873")
    private UserId nextManagerId;
}
