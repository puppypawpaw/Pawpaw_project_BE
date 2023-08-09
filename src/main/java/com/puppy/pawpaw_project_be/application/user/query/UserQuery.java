package com.puppy.pawpaw_project_be.application.user.query;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.user.domain.Role;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import com.puppy.pawpaw_project_be.exception.user.DuplicateIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQuery {
    private final UserRepository userRepository;

    public void checkDuplication(final SignUpRequest request) {
        if (userRepository.existsById(request.getId())) {
            throw new DuplicateIdException();
        }
    }

    public boolean checkUserRole(
        final UserId userId,
        final Role role
    ) {
        return userRepository.existsByUserIdAndRole(userId, role);
    }
}
