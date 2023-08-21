package kr.co.pawpaw.api.dto.auth;

import kr.co.pawpaw.domainrdb.user.domain.OAuth2Provider;
import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SocialSignUpInfoResponseTest {
    @Test
    void of() {
        //given
        OAuth2TempAttributes input = OAuth2TempAttributes.builder()
            .provider(OAuth2Provider.GOOGLE.name())
            .profileImageUrl("OAuth2ProfileImageUrl")
            .email("OAuth2Email")
            .key("123")
            .name("OAuth2Name")
            .build();

        //when
        SocialSignUpInfoResponse result = SocialSignUpInfoResponse.of(input);

        //then
        assertThat(result.getName()).isEqualTo(input.getName());
        assertThat(result.getProfileImageUrl()).isEqualTo(input.getProfileImageUrl());
    }
}