package kr.co.pawpaw.domainrdb.auth;

public enum OAuth2Provider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private String registrationId;

    OAuth2Provider(final String registrationId) {
        this.registrationId = registrationId;
    }
}
