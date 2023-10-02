package kr.co.pawpaw.mysql.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserIdTest {

    @Test
    @DisplayName("of 메서드 테스트")
    void of() {
        //given
        String uuid = UUID.randomUUID().toString();

        //when
        UserId userId = UserId.of(uuid);

        //then
        assertThat(userId.getValue()).isEqualTo(uuid);
    }
}