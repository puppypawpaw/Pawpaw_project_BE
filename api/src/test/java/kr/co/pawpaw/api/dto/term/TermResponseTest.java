package kr.co.pawpaw.api.dto.term;

import kr.co.pawpaw.mysql.term.domain.Term;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TermResponseTest {
    @Test
    @DisplayName("of 메서드 테스트")
    void of() {
        //given
        Term term = Term.builder()
            .content("term content")
            .title("term title")
            .order(1L)
            .required(true)
            .build();

        //when
        TermResponse response = TermResponse.of(term);

        //then
        assertThat(response.getId()).isEqualTo(term.getId());
        assertThat(response.getContent()).isEqualTo(term.getContent());
        assertThat(response.getTitle()).isEqualTo(term.getTitle());
        assertThat(response.getOrder()).isEqualTo(term.getOrder());
        assertThat(response.getRequired()).isEqualTo(term.getRequired());
    }
}