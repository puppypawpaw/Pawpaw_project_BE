package kr.co.pawpaw.domainrdb.storage.service.query;

import kr.co.pawpaw.domainrdb.storage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileQuery {
    private final FileRepository fileRepository;

    public boolean existsByFileName(final String fileName) {
        return fileRepository.existsById(fileName);
    }
}
