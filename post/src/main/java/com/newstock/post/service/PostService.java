package com.newstock.post.service;

import com.newstock.post.domain.post.*;
import com.newstock.post.domain.user.User;
import com.newstock.post.repository.post.*;
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
    private final LikeDislikePostRepository likeDislikePostRepository;
    private final RecentPostRepository recentPostRepository;

    public List<Post> findAboutTopic(String category){
        return postRepository.findAboutTopic(category);
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

    public List<Post> findByTopic(String topic){
        return postRepository.findByTopic(topic);
    }

    public List<Post> findByTopicTitle(String topic){
        return postRepository.findByTopicTitle(topic);
    }

    public List<Post> findByTopicContent(String topic){
        return postRepository.findByTopicContent(topic);
    }

    @Transactional
    public void checkCountAdd(Post post) {
        post.checkCount();
    }

    @Transactional
    public void addLikeCount(Post post, User user) {
        List<LikePost> likePost = likeDislikePostRepository.findLikePost(post, user);
        List<DislikePost> dislikePost = likeDislikePostRepository.findDislikePost(post, user);

        if(likePost.isEmpty() && dislikePost.isEmpty()){
            post.addLike();
            likeDislikePostRepository.saveLikePost(LikePost.makeLikePost(post,user));
            return;
        }else if(likePost.isEmpty()){
            post.addLike();
            likeDislikePostRepository.deleteDislikePost(dislikePost.get(0));
            return;
        }

        post.subLike();
        likeDislikePostRepository.deleteLikePost(likePost.get(0));
    }

    @Transactional
    public void subLikeCount(Post post, User user){
        List<DislikePost> dislikePost = likeDislikePostRepository.findDislikePost(post, user);
        List<LikePost> likePost = likeDislikePostRepository.findLikePost(post, user);

        if(dislikePost.isEmpty() && likePost.isEmpty()){
            post.subLike();
            likeDislikePostRepository.saveDislikePost(DislikePost.makeDislikePost(post,user));
            return;
        }else if(dislikePost.isEmpty()){
            post.subLike();
            likeDislikePostRepository.deleteLikePost(likePost.get(0));
            return;
        }

        post.addLike();
        likeDislikePostRepository.deleteDislikePost(dislikePost.get(0));
    }

    @Transactional
    public void addPostComment(Long postId, User user, String postCommentContent) {
        Post post = postRepository.findById(postId);
        PostComment postComment = PostComment.makePostComment(post, user, postCommentContent);
        postCommentRepository.save(postComment);
    }

    @Transactional
    public void deletePostComment(Long postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId);
        postCommentRepository.deletePostComment(postComment);
    }

    public List<Post> getMyPostList(User user) {
        return postRepository.findByUser(user);
    }

    public List<RecentPost> getRecentPostList(User user) {
        return recentPostRepository.getRecentPost(user);
    }

    public RecentPost getRecentPostAlreadySeen(Post post, User user) {
        List<RecentPost> recentPost = recentPostRepository.getRecentPostAlreadySeen(post, user);
        if(recentPost.isEmpty()){
            return null;
        }
        return recentPost.get(0);
    }

    @Transactional
    public void addRecentPost(Post post, User user) {
        RecentPost recentPost = RecentPost.makeRecentPost(post, user);
        recentPostRepository.save(recentPost);
    }

    @Transactional
    public void updateRecentPost(RecentPost recentPost){
        recentPost.setRecentPostDate();
    }

    public List<Post> findAboutTopicCount(String stock) {
        return postRepository.findAboutTopicCount(stock);
    }

    public List<Post> findAboutTopicLike(String stock) {
        return postRepository.findAboutTopicLike(stock);
    }

    public List<Post> findAboutTopicDate(String stock) {
        return postRepository.findAboutTopicDate(stock);
    }
}
