package kr.co.pawpaw.api.dto.sms;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CheckVerificationCodeResponseTest {

    @Test
    @DisplayName("of 메서드 테스트")
    void of() {
        //given
        boolean success = true;
        boolean fail = false;

        //when
        CheckVerificationCodeResponse successResponse = CheckVerificationCodeResponse.of(success);
        CheckVerificationCodeResponse failResponse = CheckVerificationCodeResponse.of(fail);

        //then
        assertThat(successResponse.isSuccess()).isTrue();
        assertThat(failResponse.isSuccess()).isFalse();
    }
}