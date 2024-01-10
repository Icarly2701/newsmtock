package com.newstock.post.domain.user;

import com.newstock.post.domain.Gender;
import com.newstock.post.domain.news.RecentNews;
import com.newstock.post.domain.post.RecentPost;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userKey;
    private String id;
    private String password;
    private String nickname;
    private Gender gender;
    private int age;

    @OneToMany(mappedBy = "user")
    private List<RecentPost> recentPost = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RecentNews> recentNews = new ArrayList<>();
}
