package com.newstock.post.repository.post;

import com.newstock.post.domain.post.TempPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempPostRepository extends JpaRepository<TempPost, Long> {
    List<TempPost> findByUserUserId(Long userId);
}
