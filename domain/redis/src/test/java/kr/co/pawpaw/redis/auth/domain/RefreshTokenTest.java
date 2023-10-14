package kr.co.pawpaw.redis.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RefreshTokenTest {
    @Test
    @DisplayName("updateTtl 메서드 테스트")
    void updateTtl() {
        //given
        RefreshToken refreshToken = RefreshToken.builder()
            .build();

        Long ttl = 360L;

        //when
        refreshToken.updateTtl(ttl);

        //then
        assertThat(refreshToken.getTtl()).isEqualTo(ttl);
    }
}