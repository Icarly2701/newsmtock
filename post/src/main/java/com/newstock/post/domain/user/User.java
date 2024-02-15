package com.newstock.post.domain.user;

import com.newstock.post.domain.Gender;
import com.newstock.post.domain.news.RecentNews;
import com.newstock.post.domain.post.RecentPost;
import com.newstock.post.domain.post.TempPost;
import com.newstock.post.dto.auth.SignupDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String id;
    private String password;
    private String nickname;
    private Gender gender;
    private int age;

    @OneToMany(mappedBy = "user")
    private List<RecentPost> recentPost = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RecentNews> recentNews = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private TempPost tempPost;

    public static User makeuser(SignupDto signupDto){
        User user = new User();
        user.age = Integer.parseInt(signupDto.getAge());
        user.id = signupDto.getId();
        user.nickname = signupDto.getNickname();
        user.password = signupDto.getPassword();
        user.gender = signupDto.getGender();
        return user;
    }
}
