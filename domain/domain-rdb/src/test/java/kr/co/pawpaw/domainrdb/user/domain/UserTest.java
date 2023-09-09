package kr.co.pawpaw.domainrdb.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {
    @Test
    @DisplayName("유저 비밀번호 변경 테스트")
    void updatePassword() {
        //given
        User user = User.builder()
            .password("default")
            .build();

        String newPassword = "new";

        //when
        user.updatePassword(newPassword);

        //then
        assertThat(user.getPassword())
            .isEqualTo(newPassword);
    }
}