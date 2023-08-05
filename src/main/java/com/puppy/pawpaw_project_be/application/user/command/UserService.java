package com.puppy.pawpaw_project_be.application.user.command;

import com.puppy.pawpaw_project_be.domain.auth.dto.request.SignUpRequest;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(final SignUpRequest request) {
        userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));
    }

    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
