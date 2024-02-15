package com.newstock.post.dto.auth;

import com.newstock.post.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupDto {

    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    private String id;
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private String nickname;

    private Gender gender;
    private String age;

    private String interestWord;
}
