package com.newstock.post.repository.post;

import com.newstock.post.domain.post.PostImage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Long postId);
    void deleteByPostContentId(Long postContentId);
    @Modifying
    @Transactional
    @Query("DELETE FROM PostImage pi WHERE pi.postContent.id = :postContentId AND pi.postImagePath IN :imagePath")
    void deleteByPostContentIdAndImagePathIn(Long postContentId, List<String> imagePath);
}
