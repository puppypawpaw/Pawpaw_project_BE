package kr.co.pawpaw.domainrdb.term.service.query;

import kr.co.pawpaw.domainrdb.term.domain.Term;
import kr.co.pawpaw.domainrdb.term.repository.TermCustomRepository;
import kr.co.pawpaw.domainrdb.term.repository.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TermQuery {
    private final TermRepository termRepository;
    private final TermCustomRepository termCustomRepository;

    public Optional<Term> findById(final Long termId) {
        return termRepository.findById(termId);
    }

    public List<Term> findAllByOrderIsIn(final Collection<Long> termOrders) {
        return termRepository.findAllByOrderIsIn(termOrders);
    }

    public List<Term> findAll() {
        return termRepository.findAll();
    }

    public List<Term> findByOrderNotNullOrderByOrder() {
        return termRepository.findByOrderNotNullOrderByOrder();
    }

    public boolean isAllRequiredTermIds(final Set<Long> termIds) {
        Set<Long> requiredTermIds = new HashSet<>(termCustomRepository.findIdByOrderNotNullAndRequiredIsTrue());

        return termIds.containsAll(requiredTermIds);
    }
}
