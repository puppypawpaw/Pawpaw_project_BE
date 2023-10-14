package kr.co.pawpaw.mysql.term.service.query;

import kr.co.pawpaw.mysql.term.domain.Term;
import kr.co.pawpaw.mysql.term.repository.TermCustomRepository;
import kr.co.pawpaw.mysql.term.repository.TermRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TermQueryTest {
    @Mock
    private TermRepository termRepository;
    @Mock
    private TermCustomRepository termCustomRepository;
    @InjectMocks
    private TermQuery termQuery;

    private Term input = Term.builder()
        .content("term1 content")
        .title("term1 title")
        .order(1L)
        .required(true)
        .build();

    private Long inputId = 1L;

    @Test
    @DisplayName("findById 메서드 테스트")
    void findById() {
        //given
        when(termRepository.findById(eq(inputId))).thenReturn(Optional.of(input));

        //when
        Optional<Term> result = termQuery.findById(inputId);

        //then
        verify(termRepository).findById(inputId);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(input);
    }

    @Test
    void findAllByOrderIsIn() {
        //given
        Collection<Long> orders = List.of(1L, 2L, 3L);
        when(termRepository.findAllByOrderIsIn(eq(orders))).thenReturn(List.of(input));

        //when
        List<Term> result = termQuery.findAllByOrderIsIn(orders);

        //then
        verify(termRepository).findAllByOrderIsIn(orders);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(input);
    }

    @Test
    void findByOrderNotNullOrderByOrder() {
        //given
        when(termRepository.findByOrderNotNullOrderByOrder()).thenReturn(List.of(input));

        //when
        List<Term> result = termQuery.findByOrderNotNullOrderByOrder();

        //then
        verify(termRepository).findByOrderNotNullOrderByOrder();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(input);
    }

    @Test
    void isAllRequiredTermIds() {
        //given
        Set<Long> input = Set.of(1L, 2L);
        List<Long> required = List.of(1L);

        when(termCustomRepository.findIdByOrderNotNullAndRequiredIsTrue()).thenReturn(required);
        //when
        boolean result = termQuery.isAllRequiredTermIds(input);

        //then
        verify(termCustomRepository).findIdByOrderNotNullAndRequiredIsTrue();
        assertThat(result).isTrue();
    }
}