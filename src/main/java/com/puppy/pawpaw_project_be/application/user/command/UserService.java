package com.puppy.pawpaw_project_be.application.user.command;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(propagation = Propagation.MANDATORY)
    public User createUser(final SignUpRequest request) {
        return userRepository.save(request.toUser(passwordEncoder.encode(request.getPassword())));
    }

    /**
     * 테스트 용도
     * 모든 유저를 삭제함
     */
    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
