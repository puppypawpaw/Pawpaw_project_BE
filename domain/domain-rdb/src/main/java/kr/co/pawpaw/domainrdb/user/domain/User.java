package kr.co.pawpaw.domainrdb.user.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.position.Position;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @EmbeddedId
    private UserId userId;

    private String email;
    private String password;
    @Column(name="real_name")
    private String name;
    private String nickname;
    private String briefIntroduction;
    private String phoneNumber;
    @Embedded
    private Position position;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private OAuth2Provider provider;
    @ManyToOne(fetch = FetchType.LAZY)
    private File userImage;

    @Builder
    public User(
        final String email,
        final String password,
        final String name,
        final String nickname,
        final String briefIntroduction,
        final String phoneNumber,
        final Position position,
        final OAuth2Provider provider,
        final LocalDateTime createdDate,
        final LocalDateTime modifiedDate,
        final File userImage
    ) {
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.userId = UserId.create();
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.name = name;
        this.nickname = nickname;
        this.briefIntroduction = briefIntroduction;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.provider = provider;
        this.userImage = userImage;
    }

    public void updateImage(final File userImage) {
        this.userImage = userImage;
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void updateProfile(
        final String nickname,
        final String briefIntroduction
    ) {
        this.nickname = nickname;
        this.briefIntroduction = briefIntroduction;
    }
}