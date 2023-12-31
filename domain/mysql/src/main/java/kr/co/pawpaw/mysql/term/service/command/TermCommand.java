package kr.co.pawpaw.mysql.term.service.command;

import kr.co.pawpaw.mysql.term.domain.Term;
import kr.co.pawpaw.mysql.term.domain.UserTermAgree;
import kr.co.pawpaw.mysql.term.repository.TermRepository;
import kr.co.pawpaw.mysql.term.repository.UserTermAgreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermCommand {
    private final TermRepository termRepository;
    private final UserTermAgreeRepository userTermAgreeRepository;

    public Term save(final Term term) {
        return termRepository.save(term);
    }

    public void deleteById(final Long termId) {
        termRepository.deleteById(termId);
    }

    public List<UserTermAgree> saveAllUserTermAgrees(final Iterable<UserTermAgree> userTermAgrees) {
        return userTermAgreeRepository.saveAll(userTermAgrees);
    }
}
