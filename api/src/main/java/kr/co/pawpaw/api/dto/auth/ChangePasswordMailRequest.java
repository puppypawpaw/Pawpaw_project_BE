package kr.co.pawpaw.api.dto.auth;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordMailRequest {
    private String name;
    private String email;
}
