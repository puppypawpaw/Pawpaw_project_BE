package kr.co.pawpaw.dynamodb.chat.service.command;

import kr.co.pawpaw.dynamodb.chat.domain.Chat;
import kr.co.pawpaw.dynamodb.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatCommand {
    private final ChatRepository chatRepository;

    public Chat save(final Chat chat) {
        return chatRepository.save(chat);
    }
}
