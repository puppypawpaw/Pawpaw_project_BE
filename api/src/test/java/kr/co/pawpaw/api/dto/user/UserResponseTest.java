package kr.co.pawpaw.api.dto.user;

import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserResponseTest {
    @Test
    @DisplayName("of 메소드 테스트")
    void of() {
        //given
        User user = User.builder()
            .email("userId")
            .nickname("username")
            .imageUrl("userImageUrl")
            .position("userPosition")
            .build();

        //when
        UserResponse response = UserResponse.of(user);

        //then
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getRole()).isEqualTo(user.getRole());
        assertThat(response.getPosition()).isEqualTo(user.getPosition());
    }
}