package kr.co.pawpaw.api.dto.sms;

import lombok.Getter;

@Getter
public class CheckVerificationCodeResponse {
    private boolean success;

    private CheckVerificationCodeResponse(final boolean success) {
        this.success = success;
    }

    public static CheckVerificationCodeResponse of(final boolean success) {
        return new CheckVerificationCodeResponse(success);
    }
}
