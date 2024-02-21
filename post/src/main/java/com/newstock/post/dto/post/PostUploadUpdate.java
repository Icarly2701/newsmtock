package com.newstock.post.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostUploadUpdate {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private List<String> imagePaths;
    private List<String> deletePaths;
    private List<MultipartFile> fileList;
}
