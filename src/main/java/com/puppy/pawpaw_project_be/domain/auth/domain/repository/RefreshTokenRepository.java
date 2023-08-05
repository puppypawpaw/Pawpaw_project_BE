package com.puppy.pawpaw_project_be.domain.auth.domain.repository;

import com.puppy.pawpaw_project_be.domain.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByValue(final String value);
    boolean existsByValue(final String value);
}
