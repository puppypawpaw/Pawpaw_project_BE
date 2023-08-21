package kr.co.pawpaw.api.dto.sms;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CheckVerificationCodeRequest {
    private String phoneNumber;
    private String code;
}
