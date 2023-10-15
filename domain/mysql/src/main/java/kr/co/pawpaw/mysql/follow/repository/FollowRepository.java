package kr.co.pawpaw.mysql.follow.repository;


import kr.co.pawpaw.mysql.follow.domain.Follow;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFromUserIdAndToUserId(String fromUserId, String toUserId);
    Long countByFromUserId(String fromUserId);
}
