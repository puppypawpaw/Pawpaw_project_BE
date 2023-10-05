package kr.co.pawpaw.mysql.reply.service.query;

import kr.co.pawpaw.mysql.reply.domain.Reply;
import kr.co.pawpaw.mysql.reply.repository.ReplyRepository;
import kr.co.pawpaw.mysql.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReplyQuery {

    private final ReplyRepository replyRepository;

    public Optional<Reply> findReplyById(Long id){
        Optional<Reply> reply = replyRepository.findReplyById(id);
        return reply;
    }

    public Optional<Reply> findReplyByIdWithParentAndUser_UserId(Long id, UserId userId){
        return replyRepository.findReplyByIdWithParentAndUser_UserId(id, userId);
    }

    public List<Reply> findRepliesWithChildren(Reply parentReply){
        return replyRepository.findRepliesWithChildren(parentReply);
    }

    public void removeReplyById(Long replyId){
         replyRepository.removeReplyById(replyId);
    }

    public Slice<Reply> findReplyListByBoardId(Long boardId, Pageable pageable){
        return replyRepository.findReplyListByBoardId(boardId, pageable);
    }
}