package kr.co.pawpaw.api.application.term.query;


import kr.co.pawpaw.common.exception.term.NotFoundTermException;
import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.domain.repository.TermCustomRepository;
import kr.co.pawpaw.domainrdb.term.domain.repository.TermRepository;
import kr.co.pawpaw.domainrdb.term.domain.repository.UserTermAgreeRepository;
import kr.co.pawpaw.domainrdb.term.dto.response.TermResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermQuery {
    private final TermRepository termRepository;
    private final TermCustomRepository termCustomRepository;
    private final UserTermAgreeRepository userTermAgreeRepository;

    @Transactional(readOnly = true)
    public TermResponse getTerm(final Long termId) {
        return termRepository.findById(termId)
            .map(TermResponse::of)
            .orElseThrow(NotFoundTermException::new);
    }


    @Transactional(readOnly = true)
    public List<Term> getAllTerms(final Collection<Long> termOrders) {
        return termRepository.findAllByOrderIsIn(termOrders);
    }

    public List<TermResponse> getAllTerms() {
        return termRepository.findByOrderNotNullOrderByOrder()
            .stream()
            .map(TermResponse::of)
            .collect(Collectors.toList());
    }

    public boolean isAllRequiredTermIds(final Set<Long> termIds) {
        Set<Long> requiredTermIds = new HashSet<>(termCustomRepository.findIdByOrderNotNullAndRequiredIsTrue());

        return termIds.containsAll(requiredTermIds);
    }
}
