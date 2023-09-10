package kr.co.pawpaw.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserEmailResponse {
    @Schema(description = "유저 이메일", type = "STRING")
    private String email;
    @Schema(description = "가입 일자", type = "STRING")
    private String registrationDate;

    public static UserEmailResponse of(
        final String email,
        final String registrationDate
    ) {
        UserEmailResponse userEmailResponse = new UserEmailResponse();
        userEmailResponse.email = email;
        userEmailResponse.registrationDate = registrationDate;

        return userEmailResponse;
    }
}
