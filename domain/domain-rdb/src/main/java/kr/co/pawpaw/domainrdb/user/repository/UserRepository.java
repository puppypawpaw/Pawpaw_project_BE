package kr.co.pawpaw.domainrdb.user.repository;

import kr.co.pawpaw.domainrdb.user.domain.OAuth2Provider;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    boolean existsByEmail(final String email);
    boolean existsByEmailAndProvider(final String email, final OAuth2Provider provider);
    boolean existsByUserIdAndRole(final UserId userId, final Role role);
    Optional<User> findByEmail(final String email);
    Optional<User> findByEmailAndProvider(
        final String email,
        final OAuth2Provider provider
    );

    Optional<User> findByNameAndPhoneNumber(
        final String name,
        final String phoneNumber
    );

    Optional<User> findByNameAndEmailAndProvider(
        final String name,
        final String email,
        final OAuth2Provider provider
    );

    boolean existsByPhoneNumber(final String phoneNumber);
}
