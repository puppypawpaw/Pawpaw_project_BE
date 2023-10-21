package kr.co.pawpaw.mysql.storage.service.query;

import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileQuery {
    private final FileRepository fileRepository;

    public Optional<File> findByFileName(final String fileName) {
        return fileRepository.findById(fileName);
    }

    public File getReferenceById(final String fileName) {
        return fileRepository.getReferenceById(fileName);
    }

    public boolean existsByFileName(final String fileName) {
        return fileRepository.existsById(fileName);
    }
}
