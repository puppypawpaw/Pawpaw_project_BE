package kr.co.pawpaw.domainrdb.user.domain;

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

    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String imageUrl;
    private String position;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private OAuth2Provider provider;

    @Builder
    public User(
        final String email,
        final String password,
        final String nickname,
        final String phoneNumber,
        final String imageUrl,
        final String position,
        final OAuth2Provider provider
    ) {
        this.userId = UserId.create();
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.position = position;
        this.provider = provider;
    }
}