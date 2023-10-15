package kr.co.pawpaw.api.dto.follow;
import kr.co.pawpaw.api.dto.pet.CreatePetRequest;
import kr.co.pawpaw.api.dto.user.UserResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class FollowDTO {
    @Getter
    public static class UserInfoWithFollow {
        private UserResponse user;
        private List<CreatePetRequest> petRequestList;
        private int followerCount;
        private int followingCount;

        @Builder
        public UserInfoWithFollow(UserResponse user, List<CreatePetRequest> petRequestList, int followerCount, int followingCount){
            this.user = user;
            this.petRequestList = petRequestList;
            this.followingCount = followingCount;
            this.followerCount = followerCount;
        }
    }


}
