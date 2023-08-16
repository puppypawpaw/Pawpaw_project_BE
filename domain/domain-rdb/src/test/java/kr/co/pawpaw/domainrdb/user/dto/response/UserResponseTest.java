package kr.co.pawpaw.domainrdb.user.dto.response;

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
            .id("userId")
            .nickname("username")
            .imageUrl("userImageUrl")
            .position("userPositoin")
            .build();

        //when
        UserResponse response = UserResponse.of(user);

        //then
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getRole()).isEqualTo(user.getRole());
        assertThat(response.getPosition()).isEqualTo(user.getPosition());
    }
}