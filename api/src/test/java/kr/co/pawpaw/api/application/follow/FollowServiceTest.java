package kr.co.pawpaw.api.application.follow;

import kr.co.pawpaw.api.config.resolver.UserIdArgumentResolver;
import kr.co.pawpaw.api.service.follow.FollowService;
import kr.co.pawpaw.domainrdb.follow.domain.Follow;
import kr.co.pawpaw.domainrdb.follow.repository.FollowRepository;
import kr.co.pawpaw.domainrdb.follow.service.query.FollowQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserQuery userQuery;
    @InjectMocks
    private FollowService followService;

    @Test
    @DisplayName("언팔로우상태인 계정을 팔로잉")
    void addFollowing(){
        String userId = "3a76193a-5277-479e-bd0f-d6ddbd5a1d16";
        Long beforeNum = followRepository.countByFromUserId(userId);
        User user = userQuery.findByUserId(UserId.of(userId)).orElseThrow();

        UserId userId1 = user.getUserId();
        String toUserId = "d1f75c67-aece-44e3-9782-310976860b65";

        Follow follow = followService.addFollowing(userId1, toUserId);
        Long afterNum = followRepository.countByFromUserId(userId);
        assertThat(follow.getFromUserId()).isEqualTo(userId);
        assertThat(follow.getToUserId()).isEqualTo(toUserId);
        assertThat(beforeNum).isEqualTo(afterNum -1);

    }
}
