package kr.co.pawpaw.dynamodb.service.chat.command;

import kr.co.pawpaw.dynamodb.domain.chat.Chat;
import kr.co.pawpaw.dynamodb.repository.ChatRepository;
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
