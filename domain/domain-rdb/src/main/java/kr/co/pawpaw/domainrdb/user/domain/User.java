package kr.co.pawpaw.domainrdb.user.domain;

import kr.co.pawpaw.domainrdb.auth.OAuth2Provider;
import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @EmbeddedId
    private UserId userId;

    private String id;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String imageUrl;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private OAuth2Provider provider;

    @Builder
    public User(
        final String id,
        final String password,
        final String nickname,
        final String phoneNumber,
        final String imageUrl,
        final OAuth2Provider provider
    ) {
        this.userId = UserId.create();
        this.id = id;
        this.password = password;
        this.role = Role.USER;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.provider = provider;
    }

    public User update(
        final String nickname,
        final String imageUrl
    ) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;

        return this;
    }
}