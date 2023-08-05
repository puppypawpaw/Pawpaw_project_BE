package com.puppy.pawpaw_project_be.domain.auth.domain;

import com.puppy.pawpaw_project_be.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributesKey;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private Oauth2Provider provider;

    @Builder
    public OAuthAttributes(
        final Map<String, Object> attributes,
        final String nameAttributesKey,
        final String name,
        final String email,
        final String phoneNumber,
        final String profileImageUrl,
        final Oauth2Provider provider
    ) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public static OAuthAttributes of(
        final Oauth2Provider provider,
        final Map<String, Object> attributes
    ) {
        if (provider.equals(Oauth2Provider.KAKAO)) {
            return ofKakao("id", attributes, provider);
        } else if (provider.equals(Oauth2Provider.GOOGLE)) {
            return ofGoogle("sub", attributes, provider);
        } else if (provider.equals(Oauth2Provider.NAVER)) {
            return ofNaver("id", attributes, provider);
        }

        return null;
    }

    private static OAuthAttributes ofGoogle(
        final String userNameAttributeName,
        final Map<String, Object> attributes,
        final Oauth2Provider provider
    ) {
        return OAuthAttributes.builder()
            .name(String.valueOf(attributes.get("name")))
            .email(String.valueOf(attributes.get("email")))
            .profileImageUrl(String.valueOf(attributes.get("picture")))
            .attributes(attributes)
            .nameAttributesKey(userNameAttributeName)
            .provider(provider)
            .build();
    }

    private static OAuthAttributes ofKakao(
        final String userNameAttributeName,
        final Map<String, Object> attributes,
        final Oauth2Provider provider
    ) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
            .name(String.valueOf(kakaoProfile.get("nickname")))
            .email(String.valueOf(kakaoAccount.get("email")))
            .profileImageUrl(String.valueOf(kakaoProfile.get("profile_image_url")))
            .nameAttributesKey(userNameAttributeName)
            .attributes(attributes)
            .provider(provider)
            .build();
    }

    public static OAuthAttributes ofNaver(
        final String userNameAttributeName,
        final Map<String, Object> attributes,
        final Oauth2Provider provider
    ) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
            .name(String.valueOf(response.get("nickname")))
            .email(String.valueOf(response.get("email")))
            .profileImageUrl(String.valueOf(response.get("profile_image")))
            .phoneNumber(String.valueOf(response.get("mobile")))
            .attributes(response)
            .nameAttributesKey(userNameAttributeName)
            .provider(provider)
            .build();
    }

    public User toEntity(final String oauth2Password) {
        return User.builder()
            .id(email)
            .nickname(name)
            .imageUrl(profileImageUrl)
            .phoneNumber(phoneNumber)
            .password(oauth2Password)
            .provider(provider)
            .build();
    }
}
