package com.newstock.post.domain.user;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "search")
@Getter
public class Search {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String searchContent;
    private LocalDateTime searchDate;
}
