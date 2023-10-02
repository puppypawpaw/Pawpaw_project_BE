package kr.co.pawpaw.mysql.reply.repository;

import kr.co.pawpaw.mysql.reply.domain.Reply;
import kr.co.pawpaw.mysql.user.domain.UserId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long>, CustomReplyRepository {

    @Query("SELECT r FROM Reply r LEFT JOIN FETCH r.parent p WHERE r.id = :id AND (p IS NULL OR p.user.userId = :userId)")
    Optional<Reply> findReplyByIdWithParentAndUser_UserId(@Param("id") Long id, @Param("userId") UserId userId);

    @EntityGraph(attributePaths = "child")
    @Query("SELECT r FROM Reply r LEFT JOIN FETCH r.child WHERE r = :parentReply")
     List<Reply> findRepliesWithChildren(@Param("parentReply") Reply parentReply);

    @Modifying
    @Query("UPDATE Reply r SET r.isRemoved = '1' WHERE r.id = :replyId")
    void removeReplyById(@Param("replyId") Long replyId);

    Optional<Reply> findReplyById(Long id);

}