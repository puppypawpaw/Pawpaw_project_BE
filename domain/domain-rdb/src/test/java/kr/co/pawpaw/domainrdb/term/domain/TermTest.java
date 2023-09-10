package kr.co.pawpaw.domainrdb.term.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TermTest {
    @Test
    @DisplayName("update 메서드 테스트")
    void update() {
        //given
        Term term = Term.builder()
            .content("term content")
            .title("term title")
            .order(1L)
            .required(true)
            .build();

        String content = "term content update";
        String title = "term title update";
        Long order = 2L;
        boolean required = false;

        //when
        term.update(title, content, required, order);

        //then
        assertThat(term.getContent()).isEqualTo(content);
        assertThat(term.getTitle()).isEqualTo(title);
        assertThat(term.getOrder()).isEqualTo(order);
        assertThat(term.getRequired()).isEqualTo(required);
    }
}