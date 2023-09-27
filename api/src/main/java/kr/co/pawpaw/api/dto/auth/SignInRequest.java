package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInRequest {
    @NotBlank
    @Schema(description = "유저의 이메일", example = "zigzaag@pawpaw.com")
    private String email;
    @NotBlank
    @Schema(description = "유저의 비밀번호", example = "abcd1234")
    private String password;
}
