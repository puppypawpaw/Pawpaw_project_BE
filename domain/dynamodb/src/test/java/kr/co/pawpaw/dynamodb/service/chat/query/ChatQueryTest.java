package kr.co.pawpaw.dynamodb.service.chat.query;

import kr.co.pawpaw.dynamodb.chat.domain.Chat;
import kr.co.pawpaw.dynamodb.chat.repository.ChatRepository;
import kr.co.pawpaw.dynamodb.chat.service.query.ChatQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("ChatQuery 클래스의")
@ExtendWith(MockitoExtension.class)
class ChatQueryTest {
    @Mock
    private ChatRepository chatRepository;
    @InjectMocks
    private ChatQuery chatQuery;

    @Nested
    @DisplayName("findWithSliceByChatroomIdAndSortIdLessThan 메서드는")
    class FindWithSliceByChatroomIdAndSortIdLessThan {
        Long sortId = 1234L;
        Long chatroomId = 123L;
        PageRequest pageable = PageRequest.of(0, 10);
        Chat chat1 = Chat.builder()
            .chatroomId(1L)
            .build();
        Chat chat2 = Chat.builder()
            .chatroomId(2L)
            .build();
        List<Chat> chatList = List.of(chat1, chat2);

        List<Chat> reverseChatList = List.of(chat2, chat1);

        Slice<Chat> chatSlice = new SliceImpl<>(chatList, pageable, true);

        @Test
        @DisplayName("sortId가 null이 아니면 ChatRepository의 findWithSliceByChatroomIdAndSortIdLessThan 메서드의 결과값을 반대로 정렬하여 반환한다.")
        void ifSortIdIsNullThenCallChatRepositoryFindWithSliceByChatroomIdAndSortIdLessThan() {
            //given
            when(chatRepository.findWithPagingByChatroomIdAndSortIdLessThan(chatroomId, sortId, pageable)).thenReturn(chatSlice);

            //when
            Slice<Chat> result = chatQuery.findWithSliceByChatroomIdAndSortIdLessThan(chatroomId, sortId, pageable);

            //then
            assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(reverseChatList);
        }

        @Test
        @DisplayName("sortId가 null이면 ChatRepository의 findWithSliceByChatroomIdAndSortIdLessThan 메서드의 결과값을 반대로 정렬하여 반환한다.")
        void ifSortIdIsNullThenCallChatRepositoryFindWithPagingByChatroomId() {
            //given
            when(chatRepository.findWithPagingByChatroomId(chatroomId, pageable)).thenReturn(chatSlice);

            //when
            Slice<Chat> result = chatQuery.findWithSliceByChatroomIdAndSortIdLessThan(chatroomId, null, pageable);

            //then
            assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(reverseChatList);
        }
    }
}