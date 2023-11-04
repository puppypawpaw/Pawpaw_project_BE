package kr.co.pawpaw.mysql.term.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.term.domain.Term;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TermRepository는")
class TermRepositoryTest extends MySQLTestContainer {
    @Autowired
    private TermRepository termRepository;

    @BeforeEach
    void beforeEach() {
        termRepository.deleteAll();
    }

    @Test
    @DisplayName("findAllByOrderIsIn 메서드 테스트")
    void findAllByOrderIsIn() {
        //given
        Term term1 = Term.builder()
            .content("term1 content")
            .title("term1 title")
            .order(1L)
            .required(true)
            .build();

        Term term2 = Term.builder()
            .content("term2 content")
            .title("term2 title")
            .order(2L)
            .required(true)
            .build();

        Term term3 = Term.builder()
            .content("term3 content")
            .title("term3 title")
            .order(3L)
            .required(false)
            .build();

        Term term4 = Term.builder()
            .content("term4 content")
            .title("term4 title")
            .order(4L)
            .required(false)
            .build();

        termRepository.saveAll(List.of(term1, term2, term3));

        List<Long> ordersToFind = List.of(2L, 3L, 4L);

        //when
        List<Term> result = termRepository.findAllByOrderIsIn(ordersToFind);

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.contains(term1)).isFalse();
        assertThat(result.contains(term2)).isTrue();
        assertThat(result.contains(term3)).isTrue();
        assertThat(result.contains(term4)).isFalse();
    }

    @Test
    void findByOrderNotNullOrderByOrder() {
        //given
        Term term1 = Term.builder()
            .content("term1 content")
            .title("term1 title")
            .order(1L)
            .required(true)
            .build();

        Term term2 = Term.builder()
            .content("term2 content")
            .title("term2 title")
            .required(true)
            .build();

        Term term3 = Term.builder()
            .content("term3 content")
            .title("term3 title")
            .order(3L)
            .required(false)
            .build();

        Term term4 = Term.builder()
            .content("term4 content")
            .title("term4 title")
            .required(false)
            .build();

        termRepository.saveAll(List.of(term4, term3, term2, term1));

        //when
        List<Term> result = termRepository.findByOrderNotNullOrderByOrder();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).equals(term1)).isTrue();
        assertThat(result.get(0).equals(term2)).isFalse();
        assertThat(result.get(0).equals(term3)).isFalse();
        assertThat(result.get(0).equals(term4)).isFalse();
        assertThat(result.get(1).equals(term1)).isFalse();
        assertThat(result.get(1).equals(term2)).isFalse();
        assertThat(result.get(1).equals(term3)).isTrue();
        assertThat(result.get(1).equals(term4)).isFalse();
    }
}