package com.newstock.post.dto;

import com.newstock.post.domain.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {
    private String id;
    private String password;
    private String nickname;
    private Gender gender;
    private int age;
    private String interestWord;
}
