package kr.co.pawpaw.mysql.chatroom.service.command;

import kr.co.pawpaw.mysql.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.mysql.chatroom.repository.ChatroomDefaultCoverRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Nested
@DisplayName("ChatroomDefaultCoverCommand는")
@ExtendWith(MockitoExtension.class)
class ChatroomDefaultCoverCommandTest {
    @Mock
    private ChatroomDefaultCoverRepository chatroomDefaultCoverRepository;
    @InjectMocks
    private ChatroomDefaultCoverCommand chatroomDefaultCoverCommand;

    private static final List<ChatroomDefaultCover> chatroomDefaultCoverList = List.of(
        ChatroomDefaultCover.builder().build()
    );

    @Nested
    @DisplayName("saveAll 메서드를 호출하면")
    class saveAll {
        @Test
        @DisplayName("ChatroomDefaultCoverRepository의 saveAll 메서드를 호출한다.")
        void callSaveAll() {
            //given
            when(chatroomDefaultCoverRepository.saveAll(chatroomDefaultCoverList)).thenReturn(chatroomDefaultCoverList);

            //when
            List<ChatroomDefaultCover> result = chatroomDefaultCoverCommand.saveAll(chatroomDefaultCoverList);

            //then
            verify(chatroomDefaultCoverRepository).saveAll(chatroomDefaultCoverList);
            assertThat(result).usingRecursiveComparison().isEqualTo(chatroomDefaultCoverList);
        }
    }
}