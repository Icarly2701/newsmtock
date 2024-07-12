package com.newstock.post.repository.post;

import com.newstock.post.domain.post.Post;

import java.util.List;

public interface CustomPostRepository {
    List<Post> findByTopic(String topic);

    List<Post> findByTopicTitle(String topic);

    List<Post> findByTopicContent(String topic);

    void deletePost(Post post);

}
