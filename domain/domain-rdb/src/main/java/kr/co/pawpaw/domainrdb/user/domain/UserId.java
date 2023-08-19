package kr.co.pawpaw.domainrdb.user.domain;

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

    private UserId(final String value) {
        this.value = value;
    }

    public static UserId create() {
        return new UserId(UUID.randomUUID().toString());
    }

    public static UserId of(final String uuid) {
        return new UserId(uuid);
    }

    @Override
    public String toString() {
        return "UserId{" +
            "value='" + value + '\'' +
            '}';
    }
}
