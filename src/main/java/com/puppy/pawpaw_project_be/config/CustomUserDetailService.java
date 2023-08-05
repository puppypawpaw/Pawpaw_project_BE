package com.puppy.pawpaw_project_be.config;

import com.puppy.pawpaw_project_be.domain.user.domain.User;
import com.puppy.pawpaw_project_be.domain.user.domain.repository.UserRepository;
import com.puppy.pawpaw_project_be.exception.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.userdetails.User.builder;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        User user = userRepository.findById(userName)
            .orElseGet(() -> userRepository.findById(userName)
                .orElseThrow(NotFoundUserException::new));

        return builder()
            .username(user.getUserId().getValue())
            .password(user.getPassword())
            .authorities(user.getRole().name())
            .build();
    }
}