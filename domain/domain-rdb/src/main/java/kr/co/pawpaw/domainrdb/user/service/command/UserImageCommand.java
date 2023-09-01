package kr.co.pawpaw.domainrdb.user.service.command;

import kr.co.pawpaw.domainrdb.user.domain.UserImage;
import kr.co.pawpaw.domainrdb.user.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserImageCommand {
    private final UserImageRepository userImageRepository;

    public UserImage save(final UserImage userImage) {
        return userImageRepository.save(userImage);
    }
}
