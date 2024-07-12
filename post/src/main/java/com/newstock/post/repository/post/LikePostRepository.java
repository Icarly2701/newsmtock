package com.newstock.post.repository.post;

import com.newstock.post.domain.post.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    List<LikePost> findByUserUserIdAndPostPostId(Long userId, Long postId);
    void deleteByPostPostId(Long postId);
}
