package com.puppy.pawpaw_project_be.application.user.query;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.user.domain.Role;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.UserId;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import com.puppy.pawpaw_project_be.exception.user.DuplicateIdException;
import com.puppy.pawpaw_project_be.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQuery {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void checkDuplication(final SignUpRequest request) {
        if (userRepository.existsById(request.getId())) {
            throw new DuplicateIdException();
        }
    }

    @Transactional(readOnly = true)
    public boolean checkUserRole(
        final UserId userId,
        final Role role
    ) {
        return userRepository.existsByUserIdAndRole(userId, role);
    }

    /**
     * 테스트 용도
     */
    @Transactional(readOnly = true)
    public User getUserById(final String id) {
        return userRepository.findById(id)
            .orElseThrow(NotFoundUserException::new);
    }
}
