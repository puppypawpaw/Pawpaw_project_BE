package kr.co.pawpaw.api.dto.auth;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    private String key;
    private String password;
}
