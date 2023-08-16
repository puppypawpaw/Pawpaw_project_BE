package kr.co.pawpaw.domainrdb.term.domain.repository;


import kr.co.pawpaw.domainrdb.term.domain.UserTermAgree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTermAgreeRepository extends JpaRepository<UserTermAgree, Long> {
    List<UserTermAgree> findAllByUserId(final String id);
}
