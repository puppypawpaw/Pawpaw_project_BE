package kr.co.pawpaw.domainrdb.term.repository;


import kr.co.pawpaw.domainrdb.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByOrderIsIn(final Collection<Long> orders);
    List<Term> findByOrderNotNullOrderByOrder();
}
