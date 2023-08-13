package com.puppy.pawpaw_project_be.domain.boardImg.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardImgDto {

    @Getter
    @Setter
    public static class BoardImgUploadDto{
        private List<MultipartFile> files;
    }

    @Getter
    @Setter
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
                return "s_" + uuid + "_" + fileName;
            }else {
                return uuid + "_" + fileName;
            }
        }
    }
}
