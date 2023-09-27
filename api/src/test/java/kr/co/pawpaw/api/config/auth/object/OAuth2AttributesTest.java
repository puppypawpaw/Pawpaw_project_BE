package kr.co.pawpaw.api.config.auth.object;

import kr.co.pawpaw.domainrdb.user.domain.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class OAuth2AttributesTest {
    private static final String kakaoEmail = "kakaoEmail";
    private static final String kakaoNickname = "kakaoNickname";
    private static final String kakaoProfileImageUrl = "kakaoProfileImageUrl";
    private static final String kakaoPassword = "kakaoPassword";
    private static final String googleName = "googleName";
    private static final String googleEmail = "googleEmail";
    private static final String googlePicture = "googlePicture";
    private static final String googlePassword = "googlePassword";
    private static final String naverNickname = "naverNickname";
    private static final String naverEmail = "naverEmail";
    private static final String naverProfileImage = "naverProfileImage";
    private static final String naverPassword = "naverPassword";

    @Test
    @DisplayName("of 메서드 테스트")
    void of() {
        //given
        Map<String, Object> kakaoAttributes = Map.of(
            "kakao_account", Map.of("email", kakaoEmail, "profile", Map.of("nickname", kakaoNickname, "profile_image_url", kakaoProfileImageUrl))
        );

        Map<String, Object> googleAttributes = Map.of(
            "name", googleName,
            "email", googleEmail,
            "picture", googlePicture
        );

        Map<String, Object> naverAttributes = Map.of(
            "response", Map.of("nickname", naverNickname, "email", naverEmail, "profile_image", naverProfileImage)
        );

        //when
        OAuth2Attributes kakaoOf = OAuth2Attributes.of(OAuth2Provider.KAKAO, kakaoAttributes);
        OAuth2Attributes naverOf = OAuth2Attributes.of(OAuth2Provider.NAVER, naverAttributes);
        OAuth2Attributes googleOf = OAuth2Attributes.of(OAuth2Provider.GOOGLE, googleAttributes);

        //then
        assertThat(Objects.requireNonNull(kakaoOf).getName()).isEqualTo(kakaoNickname);
        assertThat(Objects.requireNonNull(kakaoOf).getEmail()).isEqualTo(kakaoEmail);
        assertThat(Objects.requireNonNull(kakaoOf).getProfileImageUrl()).isEqualTo(kakaoProfileImageUrl);
        assertThat(Objects.requireNonNull(naverOf).getName()).isEqualTo(naverNickname);
        assertThat(Objects.requireNonNull(naverOf).getEmail()).isEqualTo(naverEmail);
        assertThat(Objects.requireNonNull(naverOf).getProfileImageUrl()).isEqualTo(naverProfileImage);
        assertThat(Objects.requireNonNull(googleOf).getName()).isEqualTo(googleName);
        assertThat(Objects.requireNonNull(googleOf).getEmail()).isEqualTo(googleEmail);
        assertThat(Objects.requireNonNull(googleOf).getProfileImageUrl()).isEqualTo(googlePicture);
    }

    @Test
    @DisplayName("toEntity 메서드 테스트")
    void toEntity() {
        //given
        Map<String, Object> kakaoAttributes = Map.of(
            "kakao_account", Map.of("email", kakaoEmail, "profile", Map.of("nickname", kakaoNickname, "profile_image_url", kakaoProfileImageUrl))
        );

        Map<String, Object> googleAttributes = Map.of(
            "name", googleName,
            "email", googleEmail,
            "picture", googlePicture
        );

        Map<String, Object> naverAttributes = Map.of(
            "response", Map.of("nickname", naverNickname, "email", naverEmail, "profile_image", naverProfileImage)
        );

        OAuth2Attributes kakaoOf = OAuth2Attributes.of(OAuth2Provider.KAKAO, kakaoAttributes);
        OAuth2Attributes naverOf = OAuth2Attributes.of(OAuth2Provider.NAVER, naverAttributes);
        OAuth2Attributes googleOf = OAuth2Attributes.of(OAuth2Provider.GOOGLE, googleAttributes);

        //when
        User naverUser = Objects.requireNonNull(naverOf).toEntity(naverPassword);
        User kakaoUser = Objects.requireNonNull(kakaoOf).toEntity(kakaoPassword);
        User googleUser = Objects.requireNonNull(googleOf).toEntity(googlePassword);

        //then
        assertThat(naverUser.getEmail()).isEqualTo(naverEmail);
        assertThat(naverUser.getNickname()).isEqualTo(naverNickname);
        assertThat(naverUser.getPassword()).isEqualTo(naverPassword);
        assertThat(naverUser.getProvider()).isEqualTo(OAuth2Provider.NAVER);
        assertThat(kakaoUser.getEmail()).isEqualTo(kakaoEmail);
        assertThat(kakaoUser.getNickname()).isEqualTo(kakaoNickname);
        assertThat(kakaoUser.getPassword()).isEqualTo(kakaoPassword);
        assertThat(kakaoUser.getProvider()).isEqualTo(OAuth2Provider.KAKAO);
        assertThat(googleUser.getEmail()).isEqualTo(googleEmail);
        assertThat(googleUser.getNickname()).isEqualTo(googleName);
        assertThat(googleUser.getPassword()).isEqualTo(googlePassword);
        assertThat(googleUser.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
    }
}