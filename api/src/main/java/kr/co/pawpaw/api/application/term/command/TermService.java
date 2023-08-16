package kr.co.pawpaw.api.application.term.command;

import kr.co.pawpaw.api.application.term.query.TermQuery;
import kr.co.pawpaw.common.exception.term.NotFoundTermException;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.domain.UserTermAgree;
import kr.co.pawpaw.domainrdb.term.domain.repository.TermRepository;
import kr.co.pawpaw.domainrdb.term.domain.repository.UserTermAgreeRepository;
import kr.co.pawpaw.domainrdb.term.dto.request.CreateTermRequest;
import kr.co.pawpaw.domainrdb.term.dto.request.UpdateTermRequest;
import kr.co.pawpaw.domainrdb.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermService {
    private final TermQuery termQuery;
    private final TermRepository termRepository;
    private final UserTermAgreeRepository userTermAgreeRepository;

    @Transactional
    public void createTerm(final CreateTermRequest request) {
        termRepository.save(request.toEntity());
    }

    @Transactional
    public void updateTerm(
        final Long termId,
        final UpdateTermRequest request
    ) {
        Term term = termRepository.findById(termId)
            .orElseThrow(NotFoundTermException::new);

        term.update(request);
    }

    @Transactional
    public void deleteTerm(final Long termId) {
        termRepository.deleteById(termId);
    }

    @Transactional
    public void createTermAgree(
        final List<Long> termOrders,
        final User user
    ) {
        Collection<UserTermAgree> userTermAgree = termQuery.getAllTerms(termOrders)
            .stream()
            .map(term -> UserTermAgree.builder()
                .term(term)
                .user(user)
                .build())
            .collect(Collectors.toList());

        userTermAgreeRepository.saveAll(userTermAgree);
    }
}
