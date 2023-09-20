package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordMailRequest {
    @Schema(description = "회원 실명", example = "김철수")
    private String name;
    @Schema(description = "회원 이메일", example = "pawpawdev@duckdns.org")
    private String email;
}
