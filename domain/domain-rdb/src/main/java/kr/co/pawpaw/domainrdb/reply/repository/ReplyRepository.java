package kr.co.pawpaw.domainrdb.reply.repository;

import kr.co.pawpaw.domainrdb.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long>, CustomReplyRepository {
    @Query("select r from Reply r left join fetch r.parent where r.id = :id")
    Optional<Reply> findReplyByIdWithParent(@Param("id") Long id);

    Optional<Reply> findReplyById(Long id);

}