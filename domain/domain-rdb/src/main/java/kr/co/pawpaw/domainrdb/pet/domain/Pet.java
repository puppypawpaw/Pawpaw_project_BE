package kr.co.pawpaw.domainrdb.pet.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User parent;

    private String name;

    private String introduction;

    @Enumerated(value = EnumType.STRING)
    private PetType petType;

    @Builder
    public Pet(
        final User parent,
        final String name,
        final PetType petType,
        final String introduction
    ) {
        this.parent = parent;
        this.name = name;
        this.petType = petType;
        this.introduction = introduction;
    }
}
