package kr.co.pawpaw.api.dto.term;

import kr.co.pawpaw.domainrdb.term.domain.Term;
import lombok.Getter;

@Getter
public class TermResponse {
    private Long id;
    private String title;
    private String content;
    private Boolean required;
    private Long order;

    private TermResponse(
        final Long id,
        final String title,
        final String content,
        final Boolean required,
        final Long order
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.required = required;
        this.order = order;
    }

    public static TermResponse of(final Term term) {
        return new TermResponse(
            term.getId(),
            term.getTitle(),
            term.getContent(),
            term.getRequired(),
            term.getOrder()
        );
    }
}
