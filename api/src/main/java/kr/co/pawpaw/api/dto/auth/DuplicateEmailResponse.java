package kr.co.pawpaw.api.dto.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DuplicateEmailResponse {
    private boolean duplicate;

    public static DuplicateEmailResponse of(final boolean duplicate) {
        return new DuplicateEmailResponse(duplicate);
    }
}
