package kr.co.pawpaw.domainrdb.reply.repository;

import kr.co.pawpaw.domainrdb.reply.domain.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomReplyRepository {

     Slice<Reply> findReplyListByBoardId(Long boardId, Pageable pageable);

}
