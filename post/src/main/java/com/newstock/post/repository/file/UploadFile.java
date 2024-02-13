package com.newstock.post.repository.file;

import lombok.Data;

@Data
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
    private String filePath;
    public UploadFile(String uploadFileName, String storeFileName, String filePath) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.filePath = filePath;
    }
}
