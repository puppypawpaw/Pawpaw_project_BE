package kr.co.pawpaw.mysql.term.repository;

import kr.co.pawpaw.mysql.common.MySQLTestContainer;
import kr.co.pawpaw.mysql.term.domain.Term;
import kr.co.pawpaw.mysql.term.domain.UserTermAgree;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Nested
@DisplayName("UserTermAgreeRepository는")
class UserTermAgreeRepositoryTest extends MySQLTestContainer {
    @Autowired
    private UserTermAgreeRepository userTermAgreeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TermRepository termRepository;

    @BeforeEach
    void beforeEach() {
        userTermAgreeRepository.deleteAll();
        termRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void saveAndFindTest() {
        //given
        User user1 = User.builder().build();

        User user2 = User.builder().build();

        userRepository.saveAll(List.of(user1, user2));

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

        termRepository.saveAll(List.of(term1, term2, term3));

        UserTermAgree uta1 = UserTermAgree.builder()
            .term(term1)
            .user(user1)
            .build();

        UserTermAgree uta2 = UserTermAgree.builder()
            .term(term2)
            .user(user2)
            .build();

        UserTermAgree uta3 = UserTermAgree.builder()
            .term(term3)
            .user(user2)
            .build();

        userTermAgreeRepository.saveAll(List.of(uta1, uta2));

        //when
        List<UserTermAgree> result = userTermAgreeRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.contains(uta3)).isFalse();
        assertThat(result.contains(uta2)).isTrue();
        assertThat(result.contains(uta1)).isTrue();
    }
}