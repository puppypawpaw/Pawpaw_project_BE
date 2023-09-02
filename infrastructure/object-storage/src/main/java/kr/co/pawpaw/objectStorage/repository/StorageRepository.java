package kr.co.pawpaw.objectStorage.repository;

import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface StorageRepository {
    void putObject(final String fileName, final InputStream inputStream, final ObjectMetadata objectMetadata);
    void deleteObject(final String fileName);
    String getUrl(final String fileName);
}
