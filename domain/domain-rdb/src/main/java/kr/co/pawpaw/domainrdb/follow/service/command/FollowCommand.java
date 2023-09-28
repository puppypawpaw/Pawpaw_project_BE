package kr.co.pawpaw.domainrdb.follow.service.command;

import kr.co.pawpaw.domainrdb.follow.domain.Follow;
import kr.co.pawpaw.domainrdb.follow.repository.FollowRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowCommand {
    private final FollowRepository followRepository;

    public Follow save(Follow follow){
        return followRepository.save(follow);
    }
}
