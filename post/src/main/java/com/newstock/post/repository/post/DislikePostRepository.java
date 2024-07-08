package com.newstock.post.repository.post;

import com.newstock.post.domain.post.DislikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DislikePostRepository extends JpaRepository<DislikePost, Long> {
    List<DislikePost> findByUserUserIdPostPostId(Long userId, Long postId);
    void deleteByPostId(Long postId);
}
