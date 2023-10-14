package kr.co.pawpaw.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateUserProfileRequest {
    @NotBlank
    @Schema(description = "유저 닉네임", example = "수박이")
    private String nickname;
    @NotBlank
    @Schema(description = "유저 한줄 소개", example = "3살 강쥐 수박이, 2살 앵무새 메론")
    private String briefIntroduction;
}
