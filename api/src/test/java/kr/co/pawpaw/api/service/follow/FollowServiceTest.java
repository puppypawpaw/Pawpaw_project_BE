package kr.co.pawpaw.api.service.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.domainrdb.follow.domain.Follow;
import kr.co.pawpaw.domainrdb.follow.repository.FollowRepository;
import kr.co.pawpaw.domainrdb.follow.service.command.FollowCommand;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private UserQuery userQuery;
    @Mock
    private FollowCommand followCommand;
    @Mock
    private FollowQuery followQuery;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private FollowService followService;

    private final Follow follow = Follow.builder().build();

    @Test
    @DisplayName("언팔로우상태인 계정을 팔로잉")
    void addFollowing(){
        //given
        User user1 = User.builder()
                .build();
        when(userQuery.findByUserId(user1.getUserId())).thenReturn(Optional.of(user1));

        User user2 = User.builder()
                .build();
        when(userQuery.findByUserId(user2.getUserId())).thenReturn(Optional.of(user2));
        when(followCommand.save(any(Follow.class))).thenReturn(follow);
        //when
        Follow follow1 = followService.addFollowing(user1.getUserId(), user2.getUserId().toString());

        //then
        assertThat(follow1.getToUserId()).isEqualTo(user2.getUserId().toString());



//        String userId = "3a76193a-5277-479e-bd0f-d6ddbd5a1d16";
//        Long beforeNum = followRepository.countByFromUserId(userId);
//        User user = userQuery.findByUserId(UserId.of(userId)).orElseThrow();
//
//        UserId userId1 = user.getUserId();
//        String toUserId = "d1f75c67-aece-44e3-9782-310976860b65";
//
//        Follow follow = followService.addFollowing(userId1, toUserId);
//        Long afterNum = followRepository.countByFromUserId(userId);
//        assertThat(follow.getFromUserId()).isEqualTo(userId);
//        assertThat(follow.getToUserId()).isEqualTo(toUserId);
//        assertThat(beforeNum).isEqualTo(afterNum -1);

    }
}
