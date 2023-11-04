package kr.co.pawpaw.api.dto.user;

import kr.co.pawpaw.mysql.common.domain.Position;
import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {
    private static final Position position = Position.builder()
        .latitude(36.8)
        .longitude(36.8)
        .address("36.8")
        .build();
    @Test
    @DisplayName("of 메서드 테스트")
    void of() {
        //given
        User user = User.builder()
            .email("userId")
            .nickname("username")
            .position(position)
            .build();

        String imageUrl = "imageUrl";

        //when
        UserResponse response = UserResponse.of(user, imageUrl);

        //then
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
        assertThat(response.getRole()).isEqualTo(user.getRole());
        assertThat(response.getPosition().getAddress()).isEqualTo(user.getPosition().getAddress());
        assertThat(response.getPosition().getLongitude()).isEqualTo(user.getPosition().getLongitude());
        assertThat(response.getPosition().getLatitude()).isEqualTo(user.getPosition().getLatitude());
        assertThat(response.getImageUrl()).isEqualTo(imageUrl);
    }
}