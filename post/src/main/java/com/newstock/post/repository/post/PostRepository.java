package com.newstock.post.repository.post;

import com.newstock.post.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    List<Post> findByCategory_CategoryContent(String categoryContent);

    List<Post> findByUserUserId(Long userId);

    List<Post> findByCategoryCategoryContentOrderByPostCheckCountDesc(String categoryContent);

    List<Post> findByCategoryCategoryContentOrderByPostLikeCountDesc(String categoryContent);

    List<Post> findByCategoryCategoryContentOrderByPostDateDesc(String categoryContent);

}
