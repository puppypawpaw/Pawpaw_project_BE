package com.puppy.pawpaw_project_be.domain.term.domain.repository;

import com.puppy.pawpaw_project_be.domain.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findByOrderNotNullOrderByOrder();
}
