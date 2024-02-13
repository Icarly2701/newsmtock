package com.newstock.post.service;

import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.PostComment;
import com.newstock.post.domain.post.PostImage;
import com.newstock.post.repository.PostCommentRepository;
import com.newstock.post.repository.PostImageRepository;
import com.newstock.post.repository.PostRepository;
import com.newstock.post.repository.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostImageRepository postImageRepository;

    public List<Post> findAll(){
        return postRepository.findAll();
    }

    @Transactional
    public Long savePost(Post post){
        return postRepository.save(post);
    }

    @Transactional
    public void savePostImage(PostImage postImage) {
        postImageRepository.save(postImage);
    }

    public List<PostImage> findImageByPost(Long postId){
        return postImageRepository.findByPost(postId);
    }

    public List<PostComment> findCommentByPost(Long postId){
        return postCommentRepository.findByPost(postId);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId);
    }
}
