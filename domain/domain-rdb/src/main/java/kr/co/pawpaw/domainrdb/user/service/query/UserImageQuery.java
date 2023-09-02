package kr.co.pawpaw.domainrdb.user.service.query;

import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import kr.co.pawpaw.domainrdb.user.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImageQuery {
    private final UserImageRepository userImageRepository;

    public Optional<UserImage> findByUserId(final UserId userId) {
        return userImageRepository.findByUserUserId(userId);
    }
}
