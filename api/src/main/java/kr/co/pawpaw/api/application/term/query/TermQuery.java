package kr.co.pawpaw.api.application.term.query;


import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.domain.repository.TermRepository;
import kr.co.pawpaw.domainrdb.term.domain.repository.UserTermAgreeRepository;
import kr.co.pawpaw.domainrdb.term.dto.response.TermResponse;
import kr.co.pawpaw.domainrdb.term.domain.UserTermAgree;
import kr.co.pawpaw.common.exception.term.NotFoundTermException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TermQuery {
    private final TermRepository termRepository;
    private final UserTermAgreeRepository userTermAgreeRepository;

    @Transactional(readOnly = true)
    public TermResponse getTerm(final Long termId) {
        return termRepository.findById(termId)
            .map(TermResponse::of)
            .orElseThrow(NotFoundTermException::new);
    }

    /**
     * 직접 controller에서 호출하지 않고(entity를 그대로 return하기 떄문에)
     * 다른 transactional을 가지는 method에서 호출되어야됨(그래서 mandatory 추가함)
     */
    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
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
        Set<Long> requiredTermIds = termRepository.findByOrderNotNullAndRequiredIsTrue()
            .stream()
            .map(Term::getOrder)
            .collect(Collectors.toSet());

        return termIds.containsAll(requiredTermIds);
    }

    @Transactional(readOnly = true)
    public Set<Long> getUserAgreeTermOrdersByUserId(final String id) {
        return userTermAgreeRepository.findAllByUserId(id)
            .stream()
            .map(UserTermAgree::getTerm)
            .map(Term::getOrder)
            .collect(Collectors.toSet());
    }
}
