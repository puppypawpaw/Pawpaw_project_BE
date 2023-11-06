package kr.co.pawpaw.api;

import kr.co.pawpaw.api.util.user.UserUtil;
import kr.co.pawpaw.mysql.chatroom.domain.ChatroomDefaultCover;
import kr.co.pawpaw.mysql.chatroom.service.command.ChatroomDefaultCoverCommand;
import kr.co.pawpaw.mysql.chatroom.service.query.ChatroomDefaultCoverQuery;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.domain.FileType;
import kr.co.pawpaw.mysql.storage.service.command.FileCommand;
import kr.co.pawpaw.mysql.storage.service.query.FileQuery;
import kr.co.pawpaw.mysql.term.domain.Term;
import kr.co.pawpaw.mysql.term.service.command.TermCommand;
import kr.co.pawpaw.mysql.term.service.query.TermQuery;
import kr.co.pawpaw.objectStorage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {
    private final TermCommand termCommand;
    private final TermQuery termQuery;
    private final ChatroomDefaultCoverQuery chatroomDefaultCoverQuery;
    private final ChatroomDefaultCoverCommand chatroomDefaultCoverCommand;
    private final FileCommand fileCommand;
    private final FileQuery fileQuery;
    private final StorageRepository storageRepository;

    @Override
    @Transactional
    public void run(final ApplicationArguments args) {
        insertTerm();
        insertChatroomDefaultCover();
        insertUserDefaultImage();
    }

    private void insertUserDefaultImage() {
        if (!fileQuery.existsByFileName(UserUtil.getUserDefaultImageName())) {
            fileCommand.save(File.builder()
                .byteSize(74649L)
                .type(FileType.DEFAULT)
                .contentType("image/jpeg")
                .fileName(UserUtil.getUserDefaultImageName())
                .fileUrl(storageRepository.getUrl(UserUtil.getUserDefaultImageName()))
                .build());
        }
    }

    private void insertChatroomDefaultCover() {
        if (chatroomDefaultCoverQuery.count() == 0L) {
            chatroomDefaultCoverCommand.saveAll(getNewChatroomDefaultCovers());
        }
    }

    private List<ChatroomDefaultCover> getNewChatroomDefaultCovers() {
        return insertNewChatroomDefaultCoverFile()
            .stream()
            .map(this::getChatroomDefaultCoverFunction)
            .collect(Collectors.toList());
    }

    private ChatroomDefaultCover getChatroomDefaultCoverFunction(final File file) {
        return ChatroomDefaultCover.builder()
            .coverFile(file)
            .build();
    }

    private List<File> insertNewChatroomDefaultCoverFile() {
        return fileCommand.saveAll(List.of(
            File.builder()
                .fileName("채팅방 커버1.jpg")
                .fileUrl(storageRepository.getUrl("채팅방 커버1.jpg"))
                .contentType("image/jpeg")
                .type(FileType.DEFAULT)
                .byteSize(7772L)
                .build(),
            File.builder()
                .fileName("채팅방 커버2.jpg")
                .fileUrl(storageRepository.getUrl("채팅방 커버2.jpg"))
                .contentType("image/jpeg")
                .type(FileType.DEFAULT)
                .byteSize(9195L)
                .build(),
            File.builder()
                .fileName("채팅방 커버3.jpg")
                .fileUrl(storageRepository.getUrl("채팅방 커버3.jpg"))
                .contentType("image/jpeg")
                .type(FileType.DEFAULT)
                .byteSize(11069L)
                .build(),
            File.builder()
                .fileName("채팅방 커버4.jpg")
                .fileUrl(storageRepository.getUrl("채팅방 커버4.jpg"))
                .contentType("image/jpeg")
                .type(FileType.DEFAULT)
                .byteSize(93163L)
                .build(),
            File.builder()
                .fileName("채팅방 커버5.jpg")
                .fileUrl(storageRepository.getUrl("채팅방 커버5.jpg"))
                .contentType("image/jpeg")
                .type(FileType.DEFAULT)
                .byteSize(13394L)
                .build(),
            File.builder()
                .fileName("채팅방 커버6.jpg")
                .fileUrl(storageRepository.getUrl("채팅방 커버6.jpg"))
                .contentType("image/jpeg")
                .type(FileType.DEFAULT)
                .byteSize(10321L)
                .build(),
            File.builder()
                .fileName("채팅방 커버7.jpg")
                .fileUrl(storageRepository.getUrl("채팅방 커버7.jpg"))
                .contentType("image/jpeg")
                .type(FileType.DEFAULT)
                .byteSize(20592L)
                .build()
        ));
    }

    private void insertTerm() {
        Map<Long, Term> newTerm = Map.of(
            1L, Term.builder()
                .title("만 14세 이상입니다.")
                .content("만 14세 이상입니다.")
                .order(1L)
                .required(true)
                .build(),
            2L, Term.builder()
                .title("서비스 통합이용약관 동의")
                .content("서비스 통합이용약관 동의")
                .order(2L)
                .required(true)
                .build(),
            3L, Term.builder()
                .title("위치정보 이용약관 동의")
                .content("위치정보 이용약관 동의")
                .order(3L)
                .required(true)
                .build(),
            4L, Term.builder()
                .title("개인정보 수집 및 이용 동의")
                .content("개인정보 수집 및 이용 동의")
                .order(4L)
                .required(false)
                .build()
        );

        List<Term> oldTerm = termQuery.findAll();
        Set<Long> oldOrder = oldTerm.stream()
            .map(Term::getOrder)
            .collect(Collectors.toSet());

        newTerm.forEach((order, term) -> {
            if (!oldOrder.contains(order)) {
                termCommand.save(term);
            }
        });

        oldTerm.forEach(term -> {
            Term findTerm = newTerm.get(term.getOrder());
            if (Objects.nonNull(findTerm)) {
                term.update(findTerm.getTitle(), findTerm.getContent(), findTerm.getRequired(), findTerm.getOrder());
            } else {
                termCommand.deleteById(term.getId());
            }
        });
    }
}