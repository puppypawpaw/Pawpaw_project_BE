package kr.co.pawpaw.domainrdb.follow.repository;

import kr.co.pawpaw.domainrdb.follow.domain.Follow;
import kr.co.pawpaw.domainrdb.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Long countByFromUserId(UserId fromUserId);
    Long countByToUserId(UserId toUserId);

    List<Follow> findByFromUserIdAndToUserId(String fromUserId, String toUserId);
    Long countByFromUserId(String fromUserId);
}
