package kr.co.pawpaw.domainrdb.auth;

import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private OAuth2Provider provider;

    @Builder
    public OAuth2Attributes(
        final Map<String, Object> attributes,
        final String name,
        final String email,
        final String phoneNumber,
        final String profileImageUrl,
        final OAuth2Provider provider
    ) {
        this.attributes = attributes;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public static OAuth2Attributes of(
        final OAuth2Provider provider,
        final Map<String, Object> attributes
    ) {
        if (provider.equals(OAuth2Provider.KAKAO)) {
            return ofKakao(attributes, provider);
        } else if (provider.equals(OAuth2Provider.GOOGLE)) {
            return ofGoogle(attributes, provider);
        } else if (provider.equals(OAuth2Provider.NAVER)) {
            return ofNaver(attributes, provider);
        }

        return null;
    }

    private static OAuth2Attributes ofGoogle(
        final Map<String, Object> attributes,
        final OAuth2Provider provider
    ) {
        return builder()
            .name(String.valueOf(attributes.get("name")))
            .email(String.valueOf(attributes.get("email")))
            .profileImageUrl(String.valueOf(attributes.get("picture")))
            .attributes(attributes)
            .provider(provider)
            .build();
    }

    private static OAuth2Attributes ofKakao(
        final Map<String, Object> attributes,
        final OAuth2Provider provider
    ) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return builder()
            .name(String.valueOf(kakaoProfile.get("nickname")))
            .email(String.valueOf(kakaoAccount.get("email")))
            .profileImageUrl(String.valueOf(kakaoProfile.get("profile_image_url")))
            .attributes(attributes)
            .provider(provider)
            .build();
    }

    private static OAuth2Attributes ofNaver(
        final Map<String, Object> attributes,
        final OAuth2Provider provider
    ) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return builder()
            .name(String.valueOf(response.get("nickname")))
            .email(String.valueOf(response.get("email")))
            .profileImageUrl(String.valueOf(response.get("profile_image")))
            .attributes(response)
            .provider(provider)
            .build();
    }

    public User toEntity(final String oauth2Password) {
        return User.builder()
            .id(email)
            .nickname(name)
            .imageUrl(profileImageUrl)
            .password(oauth2Password)
            .provider(provider)
            .build();
    }
}
