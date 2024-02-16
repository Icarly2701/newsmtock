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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String searchContent;
    @Column(nullable = false)
    private LocalDateTime searchDate;
}
