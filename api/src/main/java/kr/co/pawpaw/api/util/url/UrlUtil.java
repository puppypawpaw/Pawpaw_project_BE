package kr.co.pawpaw.api.util.url;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.experimental.UtilityClass;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@UtilityClass
public class UrlUtil {
    public URLConnection getConnection(final String fileUrl)  {
        try {
            return (new URL(fileUrl)).openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getInputStream(final URLConnection urlConnection) {
        try {
            return new ByteArrayInputStream(StreamUtils.copyToByteArray(urlConnection.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectMetadata getObjectMetadata(final URLConnection connection) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(connection.getContentLength());
        metadata.setContentType(connection.getContentType());

        return metadata;
    }
}
