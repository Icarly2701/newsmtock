package com.newstock.post.repository.post;

import com.newstock.post.domain.post.RecentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentPostRepository extends JpaRepository<RecentPost, Long> {
    List<RecentPost> findByUserUserIdPostPostId(Long userId, Long postId);
    List<RecentPost> findByUserUserId(Long userId);
    void deleteByPostId(Long postId);
}
