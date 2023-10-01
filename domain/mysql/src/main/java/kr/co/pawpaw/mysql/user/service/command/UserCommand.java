package kr.co.pawpaw.mysql.user.service.command;

import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommand {
    private final UserRepository userRepository;

    public User save(final User user) {
        return userRepository.save(user);
    }
}
