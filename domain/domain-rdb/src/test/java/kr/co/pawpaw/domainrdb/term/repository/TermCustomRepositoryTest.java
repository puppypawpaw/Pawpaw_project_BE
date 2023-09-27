package kr.co.pawpaw.domainrdb.term.repository;

import kr.co.pawpaw.domainrdb.config.QuerydslConfig;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { TermCustomRepository.class, QuerydslConfig.class })
@DataJpaTest
class TermCustomRepositoryTest {
    @Autowired
    private TermCustomRepository termCustomRepository;
    @Autowired
    private TermRepository termRepository;

    @BeforeEach
    void setUp() {
        termRepository.deleteAll();
    }

    @Test
    @DisplayName("findIdByOrderNotNullAndRequiredIsTrue 메서드 테스트")
    void findIdByOrderNotNullAndRequiredIsTrue() {
        //given
        Term term1 = Term.builder()
            .content("term1 content")
            .title("term1 title")
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

        termRepository.saveAll(List.of(term1, term2, term3));

        //when
        List<Long> result = termCustomRepository.findIdByOrderNotNullAndRequiredIsTrue();

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(term2.getId());
    }
}