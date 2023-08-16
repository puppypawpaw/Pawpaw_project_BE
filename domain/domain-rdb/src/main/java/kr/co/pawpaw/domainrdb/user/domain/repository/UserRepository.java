package kr.co.pawpaw.domainrdb.user.domain.repository;

import kr.co.pawpaw.domainrdb.auth.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    boolean existsById(final String id);
    boolean existsByUserIdAndRole(final UserId userId, final Role role);
    Optional<User> findById(final String id);
    Optional<User> findByIdAndProvider(
        final String id,
        final OAuth2Provider provider
    );
}
