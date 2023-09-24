package kr.co.pawpaw.domainrdb.chatroom.service.query;

import kr.co.pawpaw.domainrdb.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.domainrdb.chatroom.dto.ChatroomCoverResponse;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomDefaultCoverCustomRepository;
import kr.co.pawpaw.domainrdb.chatroom.repository.ChatroomDefaultCoverRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Nested
@DisplayName("ChatroomDefaultCoverQuery는")
class ChatroomDefaultCoverQueryTest {
    @Mock
    private ChatroomDefaultCoverRepository chatroomDefaultCoverRepository;
    @Mock
    private ChatroomDefaultCoverCustomRepository chatroomDefaultCoverCustomRepository;
    @InjectMocks
    private ChatroomDefaultCoverQuery chatroomDefaultCoverQuery;

    @Nested
    @DisplayName("count 메서드를")
    class Count {
        @Nested
        @DisplayName("호출할때 ChatroomDefaultCoverRepository의")
        class ChatroomDefaultCoverRepository {
            @Test
            @DisplayName("count 메서드를 호출하고 그대로 반환한다.")
            void count() {
                //given
                Long count = 123L;
                when(chatroomDefaultCoverRepository.count()).thenReturn(count);

                //when
                Long result = chatroomDefaultCoverQuery.count();

                //then
                assertThat(result).isEqualTo(count);
                verify(chatroomDefaultCoverRepository).count();
            }
        }
    }

    @Nested
    @DisplayName("findAllChatroomCover 메서드를")
    class findAllChatroomCover {
        @Nested
        @DisplayName("호출할때 ChatroomDefaultCoverCustomRepository의")
        class ChatroomDefaultCoverCustomRepository {
            @Test
            @DisplayName("findAllChatroomCover 메서드를 호출하고 반환값을 그대로 반환한다.")
            void callAndReturn() {
                //given
                List<ChatroomCoverResponse> responseList = List.of(
                    new ChatroomCoverResponse(123L, "coverUrl")
                );

                when(chatroomDefaultCoverCustomRepository.findAllChatroomCover()).thenReturn(responseList);
                //when
                List<ChatroomCoverResponse> result = chatroomDefaultCoverQuery.findAllChatroomCover();

                //then
                assertThat(result).usingRecursiveComparison().isEqualTo(responseList);
                verify(chatroomDefaultCoverCustomRepository).findAllChatroomCover();
            }
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class findById {
        @Nested
        @DisplayName("호출할때 ChatroomDefaultCoverRepository의")
        class ChatroomDefaultCoverRepository {
            @Test
            @DisplayName("findById 메서드를 호출하고 반환값을 그대로 반환한다.")
            void findById() {
                //given
                Long id = 123L;
                ChatroomDefaultCover cover = ChatroomDefaultCover.builder().build();
                when(chatroomDefaultCoverRepository.findById(id)).thenReturn(Optional.of(cover));

                //when
                Optional<ChatroomDefaultCover> result = chatroomDefaultCoverQuery.findById(id);

                //then
                assertThat(result.isPresent()).isTrue();
                assertThat(result.get()).usingRecursiveComparison().isEqualTo(cover);
            }
        }
    }
}