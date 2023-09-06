package kr.co.pawpaw.domainrdb.reply.service.query;

import kr.co.pawpaw.domainrdb.reply.domain.Reply;
import kr.co.pawpaw.domainrdb.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplyQuery {

    private final ReplyRepository replyRepository;

    public Optional<Reply> findById(Long id){
        Optional<Reply> reply = replyRepository.findReplyById(id);
        return reply;
    }

    public Optional<Reply> findReplyByIdWithParent(@Param("id") Long id){
      return replyRepository.findReplyByIdWithParent(id);
    }
}