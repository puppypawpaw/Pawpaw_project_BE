package kr.co.pawpaw.api.dto.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserEmailResponseTest {
    @Test
    @DisplayName("of 메서드 작동 테스트")
    void of() {
        //given
        String email = "email";
        String registrationDate = "registrationDate";

        //when
        UserEmailResponse result = UserEmailResponse.of(email, registrationDate);

        //then
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getRegistrationDate()).isEqualTo(registrationDate);
    }
}