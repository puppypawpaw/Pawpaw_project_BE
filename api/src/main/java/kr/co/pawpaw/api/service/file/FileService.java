package kr.co.pawpaw.api.service.file;

import com.amazonaws.services.s3.model.ObjectMetadata;
import kr.co.pawpaw.api.util.file.FileUtil;
import kr.co.pawpaw.api.util.url.UrlUtil;
import kr.co.pawpaw.mysql.storage.domain.File;
import kr.co.pawpaw.mysql.storage.service.command.FileCommand;
import kr.co.pawpaw.mysql.user.domain.User;
import kr.co.pawpaw.mysql.user.domain.UserId;
import kr.co.pawpaw.objectStorage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.net.URLConnection;

@Service
@RequiredArgsConstructor
public class FileService {
    private final StorageRepository storageRepository;
    private final FileCommand fileCommand;
    private final EntityManager em;

    @Transactional(propagation = Propagation.MANDATORY)
    public File saveFileByMultipartFile(final MultipartFile file, final UserId userId) {
        InputStream inputStream = FileUtil.getInputStream(file);
        ObjectMetadata metadata = FileUtil.getObjectMetadata(file);

        return saveFileByInputStream(inputStream, metadata, userId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public File saveFileByUrl(final String url, final UserId userId) {
        URLConnection urlConnection = UrlUtil.getConnection(url);
        InputStream inputStream = UrlUtil.getInputStream(urlConnection);
        ObjectMetadata metadata = UrlUtil.getObjectMetadata(urlConnection);
        return saveFileByInputStream(inputStream, metadata, userId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void deleteFileByName(final String fileName) {
        fileCommand.deleteById(fileName);
        storageRepository.deleteObject(fileName);
    }

    private String getUrl(final String fileName) {
        return storageRepository.getUrl(fileName);
    }

    private File saveFileByInputStream(
        final InputStream inputStream,
        final ObjectMetadata metadata,
        final UserId userId
    ) {
        String fileName = FileUtil.createNewFileName();

        File file = fileCommand.save(File.builder()
                .fileName(fileName)
                .fileUrl(getUrl(fileName))
                .byteSize(metadata.getContentLength())
                .contentType(metadata.getContentType())
                .uploader(em.getReference(User.class, userId))
                .build());

        storageRepository.putObject(file.getFileName(), inputStream, metadata);

        return file;
    }
}
