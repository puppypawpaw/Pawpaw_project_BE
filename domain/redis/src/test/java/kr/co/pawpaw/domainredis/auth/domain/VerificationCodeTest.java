package kr.co.pawpaw.redis.auth.domain;

import kr.co.pawpaw.redis.auth.domain.VerificationCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VerificationCodeTest {
    @Test
    @DisplayName("compositeKey 테스트")
    void compositeKey() {
        //given
        String phoneNumber = "phoneNumber";
        String usagePurpose = "usagePurpose";
        VerificationCode vCode = VerificationCode.builder()
            .phoneNumber(phoneNumber)
            .usagePurpose(usagePurpose)
            .build();

        //when
        String resultPhoneNumber = vCode.getPhoneNumber();
        String resultUsagePurpose = vCode.getUsagePurpose();

        //then
        assertThat(resultPhoneNumber).isEqualTo(phoneNumber);
        assertThat(resultUsagePurpose).isEqualTo(usagePurpose);
    }

    @Test
    @DisplayName("updateTtl 메서드 테스트")
    void updateTtl() {
        // given
        VerificationCode verificationCode = VerificationCode.builder()
            .usagePurpose("SIGN_UP")
            .code("123456")
            .phoneNumber("01012345678")
            .build();

        Long ttl = 1234L;

        // when
        verificationCode.updateTtl(ttl);

        // then
        assertThat(verificationCode.getTtl()).isEqualTo(ttl);
    }
}