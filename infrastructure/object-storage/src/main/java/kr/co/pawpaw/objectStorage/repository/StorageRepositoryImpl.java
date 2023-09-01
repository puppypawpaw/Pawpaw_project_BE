package kr.co.pawpaw.objectStorage.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import kr.co.pawpaw.objectStorage.config.StorageConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

@Repository
@RequiredArgsConstructor
public class StorageRepositoryImpl implements StorageRepository {
    private final AmazonS3 amazonS3;
    private final StorageConfig config;

    @Override
    public void putObject(
        final String fileName,
        final InputStream inputStream,
        final ObjectMetadata objectMetadata
    ) {
        try {
            if (inputStream.markSupported()) {
                inputStream.mark(Integer.MAX_VALUE);
            }

            amazonS3.putObject(config.getBucket(), fileName, inputStream, objectMetadata);

            if (inputStream.markSupported()) {
                inputStream.reset();
            }

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteObject(final String fileName) {
        amazonS3.deleteObject(config.getBucket(), fileName);
    }

    @Override
    public String getUrl(String fileName) {
        return String.join("/", config.getPublicUrl(), fileName);
    }
}
