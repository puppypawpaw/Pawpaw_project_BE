package kr.co.pawpaw.domainrdb.term.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Term extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean required;

    @Column(name = "term_order", unique = true)
    private Long order;

    @Builder
    public Term(
        final String title,
        final String content,
        final Boolean required,
        final Long order
    ) {
        this.title = title;
        this.content = content;
        this.required = required;
        this.order = order;
    }

    public void update(
        final String title,
        final String content,
        final Boolean required,
        final Long order
    ) {
        this.title = title;
        this.content = content;
        this.required = required;
        this.order = order;
    }
}
