package com.puppy.pawpaw_project_be.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId implements Serializable {
    @Column(name = "user_id")
    private String value;

    private UserId(String value) {
        this.value = value;
    }

    public static UserId create() {
        return new UserId(UUID.randomUUID().toString());
    }

    public static UserId of(String uuid) {
        return new UserId(uuid);
    }

    @Override
    public String toString() {
        return "UserId{" +
            "value='" + value + '\'' +
            '}';
    }
}
