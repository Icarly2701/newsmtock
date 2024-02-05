package com.newstock.post.dto;

import com.newstock.post.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupDto {

    @NotBlank
    private String id;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

    private Gender gender;
    private int age;

    private String interestWord;
}
