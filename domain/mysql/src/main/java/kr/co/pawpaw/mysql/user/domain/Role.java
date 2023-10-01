package kr.co.pawpaw.mysql.user.domain;

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
