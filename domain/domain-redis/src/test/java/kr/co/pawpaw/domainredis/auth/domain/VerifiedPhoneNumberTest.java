package kr.co.pawpaw.domainredis.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VerifiedPhoneNumberTest {
    @Test
    @DisplayName("updateTtl 메서드 테스트")
    void updateTtl() {
        //given
        VerifiedPhoneNumber verifiedPhoneNumber = VerifiedPhoneNumber.builder()
            .usagePurpose("SIGN_UP")
            .phoneNumber("01012345678")
            .build();
        Long ttl = 1234L;

        //when
        verifiedPhoneNumber.updateTtl(ttl);

        //then
        assertThat(verifiedPhoneNumber.getTtl()).isEqualTo(ttl);
    }
}