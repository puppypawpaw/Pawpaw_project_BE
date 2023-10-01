package kr.co.pawpaw.mysql.reply.repository;

import kr.co.pawpaw.mysql.reply.domain.Reply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomReplyRepository {

     Slice<Reply> findReplyListByBoardId(Long boardId, Pageable pageable);

}
