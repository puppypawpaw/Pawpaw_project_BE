package kr.co.pawpaw.api.controller.myPage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.co.pawpaw.api.config.annotation.AuthenticatedUserId;
import kr.co.pawpaw.api.service.follow.FollowService;
import kr.co.pawpaw.domainrdb.follow.domain.Follow;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {
    private final FollowService followService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200")
    })
    @Operation(
            method = "POST",
            summary = "팔로잉",
            description = "언팔로우된 계정을 팔로잉한다."
    )
    @PostMapping("/follower")
    public ResponseEntity<Follow> addFollowing(@AuthenticatedUserId final UserId userId, @RequestBody final String toUserId){
        Follow follow = followService.addFollowing(userId, toUserId);
        return ResponseEntity.ok(follow);
    }

}
