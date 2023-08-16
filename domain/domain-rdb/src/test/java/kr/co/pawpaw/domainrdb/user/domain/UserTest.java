package kr.co.pawpaw.domainrdb.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("update 메소드 테스트")
    void update() {
        //given
        String oldName = "name";
        String newName = "newName";
        String oldImageUrl = "oldImageUrl";
        String newImageUrl = "newImageUrl";

        User user = User.builder()
            .nickname(oldName)
            .imageUrl(oldImageUrl)
            .build();

        //when
        user.update(newName, newImageUrl);

        //then
        assertThat(user.getNickname()).isNotEqualTo(oldName);
        assertThat(user.getNickname()).isEqualTo(newName);
        assertThat(user.getNickname()).isNotEqualTo(oldImageUrl);
        assertThat(user.getImageUrl()).isEqualTo(newImageUrl);
    }
}