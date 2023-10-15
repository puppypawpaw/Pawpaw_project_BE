package kr.co.pawpaw.mysql.follow.service.command;


import kr.co.pawpaw.mysql.follow.domain.Follow;
import kr.co.pawpaw.mysql.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowCommand {
    private final FollowRepository followRepository;

    public Follow save(Follow follow){
        return followRepository.save(follow);
    }
    public void delete(Follow follow){
        followRepository.delete(follow);
    }
}
