package com.puppy.pawpaw_project_be.domain.user.domain.repository;

import com.puppy.pawpaw_project_be.domain.auth.domain.Oauth2Provider;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
    boolean existsById(final String id);
    Optional<User> findById(final String id);

    Optional<User> findByIdAndProvider(
        final String id,
        final Oauth2Provider provider
    );
}
