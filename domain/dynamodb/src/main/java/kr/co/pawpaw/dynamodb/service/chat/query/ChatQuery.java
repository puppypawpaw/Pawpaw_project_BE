package kr.co.pawpaw.dynamodb.service.chat.query;

import kr.co.pawpaw.dynamodb.domain.chat.Chat;
import kr.co.pawpaw.dynamodb.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatQuery {
    private final ChatRepository chatRepository;

    public Slice<Chat> findWithSliceByChatroomIdAndSortIdLessThan(
        final Long chatroomId,
        final Long sortId,
        final Pageable pageable
    ) {
        Slice<Chat> chatSlice;

        if (Objects.nonNull(sortId)) {
            chatSlice = chatRepository.findWithPagingByChatroomIdAndSortIdLessThan(
                chatroomId,
                sortId,
                pageable
            );
        } else {
            chatSlice = chatRepository.findWithPagingByChatroomId(
                chatroomId,
                pageable
            );
        }

        List<Chat> content = new ArrayList<>(chatSlice.getContent());
        Collections.reverse(content);

        return new SliceImpl<>(content, chatSlice.getPageable(), chatSlice.hasNext());
    }

    public Optional<Chat> findFirstByChatroomIdOrderBySortIdDesc(final Long chatroomId) {
        return chatRepository.findFirstByChatroomIdOrderBySortIdDesc(chatroomId);
    }
}
