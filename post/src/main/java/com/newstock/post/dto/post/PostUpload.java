package com.newstock.post.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostUpload {

    @NotNull
    private String title;
    @NotNull
    private String content;

    private List<MultipartFile> fileList;
}
