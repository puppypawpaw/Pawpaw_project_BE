package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @Schema(description = "비밀번호 변경 url에 있는 key", example = "874a7470-a648-4b99-927c-9a2acc5f65a5")
    private String key;
    @Schema(description = "변경 후 비밀번호", example = "dcba4321")
    private String password;
}
