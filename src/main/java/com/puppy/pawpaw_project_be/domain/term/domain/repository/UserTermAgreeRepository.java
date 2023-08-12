package com.puppy.pawpaw_project_be.domain.term.domain.repository;

import com.puppy.pawpaw_project_be.domain.term.domain.UserTermAgree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTermAgreeRepository extends JpaRepository<UserTermAgree, Long> {
    List<UserTermAgree> findAllByUserId(final String id);
}
