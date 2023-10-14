package kr.co.pawpaw.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.pawpaw.redis.auth.domain.OAuth2TempAttributes;
import lombok.Getter;

@Getter
public class SocialSignUpInfoResponse {
    @Schema(description = "소셜 유저 이름", example = "박민수")
    private String name;
    @Schema(description = "소셜 유저 이미지 url", example = "https://pub-2bd8949dbff24e3fbbc30ac222835ad6.r2.dev/874a7470-a648-4b99-927c-9a2acc5f65a5")
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
