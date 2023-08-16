package kr.co.pawpaw.domainrdb.user.domain;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private String authority;

    Role(final String authority) {
        this.authority = authority;
    }
}
