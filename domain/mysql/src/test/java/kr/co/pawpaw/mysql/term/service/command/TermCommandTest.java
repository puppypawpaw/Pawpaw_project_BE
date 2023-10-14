package kr.co.pawpaw.mysql.term.service.command;

import kr.co.pawpaw.mysql.term.domain.Term;
import kr.co.pawpaw.mysql.term.domain.UserTermAgree;
import kr.co.pawpaw.mysql.term.repository.TermRepository;
import kr.co.pawpaw.mysql.term.repository.UserTermAgreeRepository;
import kr.co.pawpaw.mysql.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TermCommandTest {
    @Mock
    private TermRepository termRepository;
    @Mock
    private UserTermAgreeRepository userTermAgreeRepository;

    @InjectMocks
    private TermCommand termCommand;

    @Test
    @DisplayName("save 메서드 테스트")
    void save() {
        //given
        Term input = Term.builder()
            .content("term1 content")
            .title("term1 title")
            .required(true)
            .build();

        when(termRepository.save(eq(input))).thenReturn(input);
        //when
        Term result = termCommand.save(input);

        //then
        verify(termRepository).save(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    @DisplayName("deleteById 메서드 테스트")
    void deleteById() {
        //given
        Long input = 1L;

        //when
        termCommand.deleteById(input);

        //then
        verify(termRepository).deleteById(1L);
    }

    @Test
    @DisplayName("saveAlluserTermAgrees 메서드 테스트")
    void saveAllUserTermAgrees() {
        //given
        Term term = Term.builder()
            .build();

        User user = User.builder()
            .build();

        List<UserTermAgree> input = List.of(UserTermAgree.builder()
            .term(term)
            .user(user)
            .build());

        when(userTermAgreeRepository.saveAll(eq(input))).thenReturn(input);

        //when
        List<UserTermAgree> result = termCommand.saveAllUserTermAgrees(input);

        //then
        verify(userTermAgreeRepository).saveAll(input);
        assertThat(result).isEqualTo(input);
    }
}