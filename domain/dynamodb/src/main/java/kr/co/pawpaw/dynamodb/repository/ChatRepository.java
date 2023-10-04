package kr.co.pawpaw.dynamodb.repository;

import kr.co.pawpaw.dynamodb.domain.chat.Chat;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

@EnableScan
public interface ChatRepository extends PagingAndSortingRepository<Chat, Chat.ChatId> {
    Slice<Chat> findWithPagingByChatroomIdAndSortIdLessThan(
        final Long chatroomId,
        final Long sortId,
        final Pageable pageable
    );

    Slice<Chat> findWithPagingByChatroomId(
        final Long chatroomId,
        final Pageable pageable
    );

    Optional<Chat> findByChatroomIdAndSortId(final Long chatroomId, final Long sortId);
}
