package kr.co.pawpaw.api.application.auth.query;

import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.repository.UserRepository;
import kr.co.pawpaw.domainrdb.user.dto.response.UserResponse;
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
