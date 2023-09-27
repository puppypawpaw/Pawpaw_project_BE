package kr.co.pawpaw.domainrdb.storage.service.command;

import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileCommand {
    private final FileRepository fileRepository;

    public File save(final File file) {
        return fileRepository.save(file);
    }

    public void deleteById(final String fileName) {
        fileRepository.deleteById(fileName);
    }

    public List<File> saveAll(final Iterable<File> files) {
        return fileRepository.saveAll(files);
    }
}
