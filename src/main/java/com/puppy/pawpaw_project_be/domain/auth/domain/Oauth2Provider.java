package com.puppy.pawpaw_project_be.domain.auth.domain;

public enum Oauth2Provider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private String registrationId;

    Oauth2Provider(final String registrationId) {
        this.registrationId = registrationId;
    }
}
