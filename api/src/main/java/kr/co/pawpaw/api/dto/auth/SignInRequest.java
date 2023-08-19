package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
public class SignInRequest {
    @NotBlank
    @Schema(description = "유저의 이메일")
    private String email;
    @NotBlank
    @Schema(description = "유저의 비밀번호")
    private String password;
}
