package kr.co.pawpaw.domainrdb.follow.domain;

import kr.co.pawpaw.domainrdb.common.BaseTimeEntity;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import lombok.*;

import javax.persistence.*;

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
}
