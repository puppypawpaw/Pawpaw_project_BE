package kr.co.pawpaw.mysql.follow.domain;


import kr.co.pawpaw.mysql.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromUserId;
    private String toUserId;

    @Builder
    public Follow(final String fromUserId, final String toUserId){
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

//    public static FollowDTO convertToDto(){
//
//    }
}
