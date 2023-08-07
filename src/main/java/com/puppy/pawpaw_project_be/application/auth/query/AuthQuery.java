package com.puppy.pawpaw_project_be.application.auth.query;

import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import com.puppy.pawpaw_project_be.domain.user.dto.response.UserResponse;
import com.puppy.pawpaw_project_be.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthQuery {
    private final UserRepository userRepository;

    public UserResponse whoAmI(final UserId userId) {
        return userRepository.findById(userId)
            .map(UserResponse::of)
            .orElseThrow(NotFoundUserException::new);
    }
}
