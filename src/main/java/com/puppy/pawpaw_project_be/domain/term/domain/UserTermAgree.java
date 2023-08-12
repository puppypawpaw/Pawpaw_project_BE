package com.puppy.pawpaw_project_be.domain.term.domain;

import com.puppy.pawpaw_project_be.domain.common.BaseTimeEntity;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermAgree extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Term term;

    @Builder
    public UserTermAgree(
        final User user,
        final Term term
    ) {
        this.user = user;
        this.term = term;
    }
}
