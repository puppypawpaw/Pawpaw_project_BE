package com.puppy.pawpaw_project_be.application.user.query;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
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
@Transactional(readOnly = true)
public class UserQuery {
    private final UserRepository userRepository;

    public void checkDuplication(final SignUpRequest request) {
        if (userRepository.existsById(request.getId())) {
            throw new DuplicateIdException();
        }
    }

    public UserId getUserIdByOAuthEmail(final String email) {
        return userRepository.findById(email)
            .map(User::getUserId)
            .orElseThrow(NotFoundUserException::new);
    }
}
