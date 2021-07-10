package hcy.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFileName; // 고객이 정한 아이템 이름.
    private String storeFileName; // 서버에 저장되는 아이템 이름.

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
