package kr.co.pawpaw.api.dto.boardImg;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardImgDto {

    @Getter
    public static class BoardImgUploadDto{
        private List<MultipartFile> files;

        public void setFiles(List<MultipartFile> files) {
            this.files = files;
        }
    }
}
