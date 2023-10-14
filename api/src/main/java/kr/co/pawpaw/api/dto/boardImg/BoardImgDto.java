package kr.co.pawpaw.api.dto.boardImg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardImgDto {

    @Getter
    public static class BoardImgUploadDto{
        @Schema(description = "업로드할 이미지 파일 목록")
        private List<MultipartFile> files;

        public void setFiles(List<MultipartFile> files) {
            this.files = files;
        }
    }
}
