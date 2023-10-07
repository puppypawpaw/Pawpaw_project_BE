package kr.co.pawpaw.redis.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ChangePasswordTempKeyTest {
    @Test
    @DisplayName("updateTtl 메서드 - ttl을 업데이트 한다.")
    void updateTtl() {
        //given
        ChangePasswordTempKey changePasswordTempKey = ChangePasswordTempKey.builder()
            .build();
        Long ttl = 123L;
        //when
        changePasswordTempKey.updateTtl(ttl);

        //then
        assertThat(changePasswordTempKey.getTtl()).isEqualTo(ttl);
    }
}