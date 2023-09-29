package kr.co.pawpaw.api.service.term;

import kr.co.pawpaw.api.dto.term.CreateTermRequest;
import kr.co.pawpaw.api.dto.term.TermResponse;
import kr.co.pawpaw.api.dto.term.UpdateTermRequest;
import kr.co.pawpaw.common.exception.term.NotFoundTermException;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.service.command.TermCommand;
import kr.co.pawpaw.domainrdb.term.service.query.TermQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TermServiceTest {
    @Mock
    private TermQuery termQuery;
    @Mock
    private TermCommand termCommand;
    @Mock
    private CreateTermRequest createTermRequest;
    @InjectMocks
    private TermService termService;

    private Term term1 = Term.builder()
        .title("term1-title")
        .content("term1-content")
        .required(true)
        .order(1L)
        .build();

    private Term term2 = Term.builder()
        .title("term2-title")
        .content("term2-content")
        .required(true)
        .order(2L)
        .build();

    private Term term3 = Term.builder()
        .title("term3-title")
        .content("term3-content")
        .required(false)
        .order(3L)
        .build();


    @Test
    @DisplayName("createTerm 메서드 termCommand save 메서드 호출 테스트")
    void createTerm() {
        //given
        when(createTermRequest.toEntity()).thenReturn(term1);

        //when
        termService.createTerm(createTermRequest);

        //then
        verify(termCommand).save(term1);
    }

    @Test
    @DisplayName("getAllTerms 메서드 termQuery findByOrderNotNullOrderByOrder 메서드 호출 테스트")
    void getAllTerms() {
        //given
        List<Term> termList = List.of(term1, term2, term3);
        when(termQuery.findByOrderNotNullOrderByOrder()).thenReturn(termList);
        List<TermResponse> resultExpected = termList.stream()
            .map(TermResponse::of)
            .collect(Collectors.toList());

        //when
        List<TermResponse> result = termService.getAllTerms();

        //then
        verify(termQuery).findByOrderNotNullOrderByOrder();
        assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
    }

    @Test
    @DisplayName("getTerm 메서드 termQuery findById 메서드 호출 테스트")
    void getTerm() {
        //given
        Long id = 1L;
        when(termQuery.findById(id)).thenReturn(Optional.of(term1));
        TermResponse resultExpected = TermResponse.of(term1);

        //when
        TermResponse result = termService.getTerm(id);

        //then
        verify(termQuery).findById(id);
        assertThat(result).usingRecursiveComparison().isEqualTo(resultExpected);
    }

    @Test
    @DisplayName("getTerm 메서드 NotFoundTermException 발생 테스트")
    void getTermNotFoundTermException() {
        //given
        Long id = 1L;
        when(termQuery.findById(id)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> termService.getTerm(id)).isInstanceOf(NotFoundTermException.class);

        //then
        verify(termQuery).findById(id);
    }

    @Test
    @DisplayName("updateTerm 메서드 updateTermNotFoundTermException 발생 테스트")
    void updateTermNotFoundTermException() {
        //given
        Long id = 1L;
        when(termQuery.findById(id)).thenReturn(Optional.empty());
        UpdateTermRequest request = UpdateTermRequest.builder()
            .title("update title")
            .content("update content")
            .order(2L)
            .required(false)
            .build();

        //when
        assertThatThrownBy(() -> termService.updateTerm(id, request)).isInstanceOf(NotFoundTermException.class);

        //then
        verify(termQuery).findById(id);
    }

    @Test
    @DisplayName("updateTerm 메서드 termQuery findById 호출 테스트")
    void updateTermCallTermQueryFindById() {
        //given
        Long id = 1L;

        when(termQuery.findById(id)).thenReturn(Optional.of(term1));
        UpdateTermRequest request = UpdateTermRequest.builder()
            .title("update title")
            .content("update content")
            .order(2L)
            .required(false)
            .build();

        //when
        termService.updateTerm(id, request);

        //then
        verify(termQuery).findById(id);
        assertThat(term1).usingRecursiveComparison()
            .ignoringFieldsMatchingRegexes("createdDate", "modifiedDate", "id")
            .isEqualTo(request);
    }

    @Test
    @DisplayName("deleteTerm 메서드 termCommand deleteById 호출 테스트")
    void deleteTerm() {
        //given
        Long id = 1L;
        //when
        termService.deleteTerm(id);

        //then
        verify(termCommand).deleteById(id);
    }
}