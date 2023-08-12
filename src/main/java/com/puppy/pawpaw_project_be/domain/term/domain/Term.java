package com.puppy.pawpaw_project_be.domain.term.domain;

import com.puppy.pawpaw_project_be.domain.common.BaseTimeEntity;
import com.puppy.pawpaw_project_be.domain.term.dto.request.UpdateTermRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.reflect.Field;

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

    public void update(final UpdateTermRequest request) {
        for (Field field : request.getClass().getDeclaredFields()) {
            try {
                Field thisField = this.getClass().getDeclaredField(field.getName());
                thisField.setAccessible(true);
                thisField.set(this, field.get(request));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // ignore update field
            }
        }
    }
}
