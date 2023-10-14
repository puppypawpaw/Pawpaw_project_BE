package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DuplicateEmailResponse {
    @Schema(description = "이메일 중복 여부")
    private boolean duplicate;

    public static DuplicateEmailResponse of(final boolean duplicate) {
        return new DuplicateEmailResponse(duplicate);
    }
}
