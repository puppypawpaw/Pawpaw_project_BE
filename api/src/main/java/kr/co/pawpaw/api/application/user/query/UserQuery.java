package kr.co.pawpaw.api.application.user.query;

import kr.co.pawpaw.common.exception.user.DuplicateIdException;
import kr.co.pawpaw.domainrdb.auth.dto.request.SignUpRequest;
import kr.co.pawpaw.domainrdb.user.domain.Role;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.repository.UserRepository;
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
}
