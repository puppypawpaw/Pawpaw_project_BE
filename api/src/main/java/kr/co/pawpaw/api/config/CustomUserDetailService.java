package kr.co.pawpaw.api.config;

import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import static org.springframework.security.core.userdetails.User.builder;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        User user = userRepository.findById(userName)
            .orElseThrow(NotFoundUserException::new);

        return builder()
            .username(user.getUserId().getValue())
            .password(user.getPassword())
            .authorities(user.getRole().name())
            .build();
    }
}