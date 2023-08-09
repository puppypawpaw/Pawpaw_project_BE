package com.puppy.pawpaw_project_be.domain.pet.domain;

import com.puppy.pawpaw_project_be.domain.common.BaseTimeEntity;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
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

    @Builder
    public Pet(
        final User parent,
        final String name,
        final String introduction
    ) {
        this.parent = parent;
        this.name = name;
        this.introduction = introduction;
    }
}
