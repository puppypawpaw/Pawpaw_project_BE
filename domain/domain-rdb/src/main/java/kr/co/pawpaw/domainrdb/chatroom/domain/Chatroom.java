package kr.co.pawpaw.domainrdb.chatroom.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    @Column(nullable = false)
    private Boolean searchable;
    @Column(nullable = false)
    private Boolean locationLimit;

    @Builder
    public Chatroom(
        final String name,
        final String description,
        final Boolean searchable,
        final Boolean locationLimit
    ) {
        this.name = name;
        this.description = description;
        this.searchable = searchable;
        this.locationLimit = locationLimit;
    }
}
