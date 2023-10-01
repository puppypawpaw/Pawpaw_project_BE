package kr.co.pawpaw.mysql.term.repository;


import kr.co.pawpaw.mysql.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByOrderIsIn(final Collection<Long> orders);
    List<Term> findByOrderNotNullOrderByOrder();
}
