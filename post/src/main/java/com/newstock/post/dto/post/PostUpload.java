package com.newstock.post.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostUpload {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private List<MultipartFile> fileList;
}
