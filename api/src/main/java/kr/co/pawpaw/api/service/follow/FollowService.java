package kr.co.pawpaw.api.service.follow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pawpaw.common.exception.user.NotFoundUserException;
import kr.co.pawpaw.domainrdb.follow.domain.Follow;
import kr.co.pawpaw.domainrdb.follow.service.command.FollowCommand;
import kr.co.pawpaw.domainrdb.follow.service.query.FollowQuery;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import kr.co.pawpaw.domainrdb.user.service.query.UserQuery;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    
    private final UserQuery userQuery;
    private final FollowQuery followQuery;
    private final FollowCommand followCommand;
    private final ObjectMapper objectMapper;
    
    @SneakyThrows
    public Follow addFollowing(UserId userId, String toUserId){
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        String fromUserId = user.getUserId().getValue();
        JsonNode jsonNode = objectMapper.readTree(toUserId);
        toUserId = jsonNode.get("toUserId").asText();

        if(!followQuery.findByFromUserIdAndToUserId(fromUserId,toUserId).isEmpty()) {
            throw new RuntimeException("이미 팔로우한 계정입니다.");
        }

        Follow follow = Follow.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .build();

            return followCommand.save(follow);
        };

//    @SneakyThrows
    public void unfollowing(UserId userId, String toUserId){
        User user = userQuery.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        String fromUserId = user.getUserId().getValue();

//        JsonNode jsonNode = objectMapper.readTree(toUserId);
//        toUserId = jsonNode.get("toUserId").asText();

        if(!followQuery.findByFromUserIdAndToUserId(fromUserId,toUserId).isEmpty()) {
            List<Follow> byFromUserIdAndToUserId = followQuery.findByFromUserIdAndToUserId(fromUserId, toUserId);
            byFromUserIdAndToUserId.stream().forEach(follow -> {
                followCommand.delete(follow);
            });

        }else{
            throw new RuntimeException("팔로우한 계정만 언팔로우 가능합니다.");
        }
    }

    }

