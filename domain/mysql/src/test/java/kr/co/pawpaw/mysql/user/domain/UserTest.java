package kr.co.pawpaw.mysql.user.domain;

import kr.co.pawpaw.mysql.storage.domain.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User 의")
class UserTest {
    User user = User.builder()
        .password("default")
        .build();

    @Nested
    @DisplayName("updatePassword 메서드는")
    class updatePassword {
        @Test
        @DisplayName("호출 시 password를 변경한다.")
        void changePassword() {
            //given
            String newPassword = "new";

            //when
            user.updatePassword(newPassword);

            //then
            assertThat(user.getPassword())
                .isEqualTo(newPassword);
        }
    }

    @Nested
    @DisplayName("updateImage 메서드는")
    class updateImage {
        @Test
        @DisplayName("호출 시 userImage를 변경한다.")
        void changeUserImage() {
            //given
            File newImageFile = File.builder().build();

            //when
            user.updateImage(newImageFile);

            //then
            assertThat(user.getUserImage())
                .isEqualTo(newImageFile);
        }
    }

    @Nested
    @DisplayName("updateProfile 메서드는")
    class updateProfile {
        @Test
        @DisplayName("호출 시 nickname과 briefIntroduction을 변경한다.")
        void changeNicknameAndBriefIntroduction() {
            //given
            String nickname = "nick";
            String briefIntroduction = "I'm nick";

            //when
            user.updateProfile(nickname, briefIntroduction);

            //then
            assertThat(user.getNickname())
                .isEqualTo(nickname);
            assertThat(user.getBriefIntroduction())
                .isEqualTo(briefIntroduction);
        }
    }
}