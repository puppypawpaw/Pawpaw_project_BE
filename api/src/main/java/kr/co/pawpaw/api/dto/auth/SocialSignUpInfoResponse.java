package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.domainredis.auth.domain.OAuth2TempAttributes;
import lombok.Getter;

@Getter
public class SocialSignUpInfoResponse {
    @Schema(description = "소셜 유저 이름")
    private String name;
    @Schema(description = "소셜 유저 이미지 url")
    private String profileImageUrl;

    private SocialSignUpInfoResponse(
        final String name,
        final String profileImageUrl
    ) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public static SocialSignUpInfoResponse of(final OAuth2TempAttributes oAuth2TempAttributes) {
        return new SocialSignUpInfoResponse(
            oAuth2TempAttributes.getName(),
            oAuth2TempAttributes.getProfileImageUrl()
        );
    }
}
