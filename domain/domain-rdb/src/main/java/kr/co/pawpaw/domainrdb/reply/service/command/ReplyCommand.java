package kr.co.pawpaw.domainrdb.reply.service.command;

import kr.co.pawpaw.domainrdb.reply.domain.Reply;
import kr.co.pawpaw.domainrdb.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyCommand {

    private final ReplyRepository replyRepository;

    public Reply save(Reply reply){
        return replyRepository.save(reply);
    }
}
