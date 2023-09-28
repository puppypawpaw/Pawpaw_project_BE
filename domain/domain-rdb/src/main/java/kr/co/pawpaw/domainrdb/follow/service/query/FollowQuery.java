package kr.co.pawpaw.domainrdb.follow.service.query;

import kr.co.pawpaw.domainrdb.follow.domain.Follow;
import kr.co.pawpaw.domainrdb.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowQuery {
    private final FollowRepository followRepository;

    public List<Follow> findByFromUserIdAndToUserId(String fromUserId, String toUserId){
        return followRepository.findByFromUserIdAndToUserId(fromUserId,toUserId);
    }
}
