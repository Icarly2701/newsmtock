package com.newstock.post.repository.post;

import com.newstock.post.domain.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostPostId(Long postId);
    void deleteByPostPostId(Long postId);
}
