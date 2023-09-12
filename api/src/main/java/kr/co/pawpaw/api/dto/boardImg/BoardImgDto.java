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

    @Getter
    public static class BoardImgResultDto {
        private String uuid;
        private String fileName;
        private boolean img;


        public BoardImgResultDto(String uuid, String fileName, boolean img) {
            this.uuid = uuid;
            this.fileName = fileName;
            this.img = img;
        }


        public String getLink(){
            if (img){
                return uuid + "_" + fileName;
            }else {
                return "s_" + uuid + "_" + fileName;
            }
        }
    }
}
