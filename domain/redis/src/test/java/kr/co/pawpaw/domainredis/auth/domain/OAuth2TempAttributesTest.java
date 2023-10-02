package kr.co.pawpaw.redis.auth.domain;

import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OAuth2TempAttributesTest {
    @Test
    @DisplayName("updateTtl 메서드 테스트")
    void updateTtl() {
        //given
        OAuth2TempAttributes oAuth2TempAttributes = OAuth2TempAttributes.builder()
            .build();

        Long ttl = 3L;

        //when
        oAuth2TempAttributes.updateTtl(ttl);

        //then
        assertThat(oAuth2TempAttributes.getTtl()).isEqualTo(ttl);
    }
}