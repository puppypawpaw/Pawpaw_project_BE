package kr.co.pawpaw.domainrdb.chatroom.service.command;


import kr.co.pawpaw.domainrdb.chatroom.domain.TrandingChatroom;
import kr.co.pawpaw.domainrdb.chatroom.repository.TrandingChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrandingChatroomCommand {
    private final TrandingChatroomRepository trandingChatroomRepository;

    public TrandingChatroom save(final TrandingChatroom trandingChatroom) {
        return trandingChatroomRepository.save(trandingChatroom);
    }
}
