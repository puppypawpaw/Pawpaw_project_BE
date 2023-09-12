package kr.co.pawpaw.api.application.file;

import com.amazonaws.services.s3.model.ObjectMetadata;
import kr.co.pawpaw.domainrdb.storage.domain.File;
import kr.co.pawpaw.domainrdb.storage.service.command.FileCommand;
import kr.co.pawpaw.domainrdb.user.domain.User;
import kr.co.pawpaw.objectStorage.repository.StorageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    StorageRepository storageRepository;
    @Mock
    FileCommand fileCommand;
    @Mock
    EntityManager em;
    @InjectMocks
    FileService fileService;
    @Mock
    MultipartFile multipartFile;


    @Test
    @DisplayName("MultipartFile 저장 테스트")
    void saveFileByMultipartFile() throws IOException {
        //given
        InputStream inputStream = new ByteArrayInputStream("Mock file content".getBytes());
        when(multipartFile.getInputStream()).thenReturn(inputStream);
        when(multipartFile.getSize()).thenReturn(123L);
        when(multipartFile.getContentType()).thenReturn("image/png");

        User user = User.builder()
            .build();
        when(em.getReference(eq(User.class), eq(user.getUserId()))).thenReturn(user);

        File file = File.builder()
            .fileName(UUID.randomUUID().toString())
            .uploader(user)
            .byteSize(123L)
            .contentType("image/png")
            .build();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(123L);
        metadata.setContentType("image/png");

        when(fileCommand.save(any(File.class))).thenReturn(file);

        //when
        File savedFile = fileService.saveFileByMultipartFile(multipartFile, user.getUserId());

        //then
        verify(multipartFile).getInputStream();
        verify(multipartFile).getSize();
        verify(multipartFile).getContentType();
        verify(em).getReference(User.class, user.getUserId());
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        verify(fileCommand).save(fileCaptor.capture());
        assertThat(fileCaptor.getValue()).usingRecursiveComparison().ignoringFieldsMatchingRegexes("fileName").isEqualTo(file);
        ArgumentCaptor<ObjectMetadata> metadataCaptor = ArgumentCaptor.forClass(ObjectMetadata.class);
        verify(storageRepository).putObject(eq(file.getFileName()), eq(inputStream), metadataCaptor.capture());
        assertThat(metadataCaptor.getValue()).usingRecursiveComparison().isEqualTo(metadata);
        assertThat(savedFile).usingRecursiveComparison().isEqualTo(file);
    }

    @Test
    @DisplayName("saveFileByUrl 메서드 테스트")
    void saveFileByUrl() {
        //given
        User user = User.builder()
            .build();
        File file = File.builder()
            .fileName(UUID.randomUUID().toString())
            .uploader(user)
            .byteSize(123L)
            .contentType("image/png")
            .build();
        when(em.getReference(eq(User.class), eq(user.getUserId()))).thenReturn(user);
        when(fileCommand.save(any(File.class))).thenReturn(file);

        //when
        File savedFile = fileService.saveFileByUrl("https://picsum.photos/536/354", user.getUserId());

        //then
        verify(em).getReference(User.class, user.getUserId());
        verify(fileCommand).save(any(File.class));
        verify(storageRepository).putObject(eq(file.getFileName()), any(InputStream.class), any(ObjectMetadata.class));
        assertThat(savedFile).usingRecursiveComparison().isEqualTo(file);
    }

    @Test
    @DisplayName("deleteFileByName 메서드 테스트")
    void deleteFileByName() {
        //given
        String fileName = "fileName";

        //when
        fileService.deleteFileByName(fileName);

        //then
        verify(fileCommand).deleteById(fileName);
        verify(storageRepository).deleteObject(fileName);
    }

    @Test
    @DisplayName("getUrl 메서드 테스트")
    void getUrl() {
        //given
        String fileName = "fileName";
        String urlExpected = "okay";
        when(storageRepository.getUrl(fileName)).thenReturn(urlExpected);
        //when
        String url = fileService.getUrl(fileName);

        //then
        verify(storageRepository).getUrl(fileName);
        assertThat(url).isEqualTo(urlExpected);
    }
}