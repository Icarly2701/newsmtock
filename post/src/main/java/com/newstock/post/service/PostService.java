package com.newstock.post.service;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.Target;
import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.RecentNews;
import com.newstock.post.domain.post.*;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.post.PostDto;
import com.newstock.post.dto.post.PostUpload;
import com.newstock.post.dto.post.PostUploadUpdate;
import com.newstock.post.repository.file.FileStore;
import com.newstock.post.repository.file.UploadFile;
import com.newstock.post.repository.post.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final TempPostRepository tempPostRepository;
    private final FileStore fileStore;

    public List<Post> findAboutTopic(String category){
        return postRepository.findAboutTopic(category);
    }

    private Long savePost(Post post){
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

    private void checkCountAdd(Post post) {
        post.checkCount();
    }

    private void addLikeCount(Post post, User user) {
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

    private void subLikeCount(Post post, User user){
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

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId);
        Long postContentId = post.getPostContent().getPostContentId();
        likeDislikePostRepository.deleteLikePost(postId);
        likeDislikePostRepository.deleteDislikePost(postId);
        recentPostRepository.deleteByPost(postId);
        postCommentRepository.deleteByPost(postId);
        postImageRepository.deleteByPostContentId(postContentId);
        postRepository.deletePost(post);
    }

    private void updatePost(Post post, PostUploadUpdate postUpload) {
        post.updatePost(postUpload);
    }

    private void updatePostImage(Long postContentId, List<String> imagePath) {
        postImageRepository.deleteByPostContentIdExceptImagePath(postContentId, imagePath);
    }

    @Transactional
    public void saveCategory(Category category){
        postRepository.saveCategory(category);
    }

    private void saveTempPost(TempPost tempPost) {
        tempPostRepository.save(tempPost);
    }

    public Category findCategory(String category) {
        return postRepository.findCategory(category);
    }

    public TempPost findByUser(User user){
        return tempPostRepository.findByUser(user);
    }

    private void deleteTempPost(TempPost tempPost){
        tempPostRepository.deletePost(tempPost);
    }

    public List<Post> getSearchData(String keyword, Target target) {
        return target == null ? this.findByTopic(keyword) : switch (target) {
            case title_content -> this.findByTopic(keyword);
            case title -> this.findByTopicTitle(keyword);
            default -> this.findByTopicContent(keyword);
        };
    }

    @Transactional
    public Long processAddPost(User user, String category, PostUpload postUpload) {
        Post post = Post.makePost(new PostDto(postUpload.getTitle(),
                postUpload.getContent(),
                this.findCategory(category)), user);
        this.saveImage(postUpload.getFileList(), post);
        return this.savePost(post);
    }

    @Transactional
    public void processUpdatePost(Long postId, PostUploadUpdate postUpload) {
        Post post = this.findById(postId);
        this.updatePost(post, postUpload);
        this.updatePostImage(post.getPostContent().getPostContentId(), postUpload.getImagePaths());
        saveImage(postUpload.getFileList(), post);
    }

    private void saveImage(List<MultipartFile> fileList, Post post) {
        // 실제 파일 저장
        List<UploadFile> uploadFiles = fileStore.storeFiles(fileList);
        //데이터베이스에 파일 이름 저장
        for(UploadFile uploadFile: uploadFiles){
            PostImage postImage = PostImage.makePostImage(uploadFile.getFilePath(), post.getPostContent());
            this.savePostImage(postImage);
        }
    }

    @Transactional
    public void processRecommend(Long postId, User user, String action) {
        Post post = this.findById(postId);
        if(action.equals("like")){
            this.addLikeCount(post, user);
            return;
        }
        this.subLikeCount(post, user);
    }

    @Transactional
    public void processTempPost(User user, String category, PostUploadUpdate postUpload) {
        // 기본적인 post 저장
        PostDto postDto = new PostDto(
                postUpload.getTitle(),
                postUpload.getContent(),
                this.findCategory(category));

        TempPost tempPost = TempPost.tempPost(postDto,user);
        TempPost findTempPost = this.findByUser(user);
        if(findTempPost != null){
            this.deleteTempPost(findTempPost);
        }
        this.saveTempPost(tempPost);
    }

    @Transactional
    public Post processDetailPagePost(Long postId, User user, String isLike) {
        Post post = this.findById(postId);
        if(user != null) this.processRecentPost(post, user);
        if(isLike.isEmpty()) this.checkCountAdd(post);
        return post;
    }

    private void processRecentPost(Post post, User user) {
        RecentPost recentPost = this.getRecentPostAlreadySeen(post, user);
        if(recentPost == null) {
            this.addRecentPost(post, user);
            return;
        }
        this.updateRecentPost(recentPost);
    }
}
