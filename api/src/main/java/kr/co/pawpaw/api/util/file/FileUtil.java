package kr.co.pawpaw.api.util.file;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@UtilityClass
public class FileUtil {
    public String createNewFileName() {
        return UUID.randomUUID().toString();
    }

    public InputStream getInputStream(final MultipartFile file) {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectMetadata getObjectMetadata(final MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        return metadata;
    }

    public int getByteLength(final MultipartFile file) {
        try {
            return file.getBytes().length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
