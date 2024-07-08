package com.newstock.post.service;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.Target;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostService {

    //private final CustomPostRepositoryImpl customPostRepositoryImpl;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostImageRepository postImageRepository;
    private final LikePostRepository likePostRepository;
    private final DislikePostRepository dislikePostRepository;
    private final RecentPostRepository recentPostRepository;
    private final TempPostRepository tempPostRepository;
    private final FileStore fileStore;
    @Value("${aws.bucket-name}")
    private String bucketName;

    public List<RecentPost> getRecentPostList(User user) {
        return recentPostRepository.findByUserUserId(user.getUserId());
    }

    public List<Post> getMyPostList(User user) {
        return postRepository.findByUserUserId(user.getUserId());
    }

    public List<Post> getSearchData(String keyword, Target target) {
        return target == null ? this.findByTopic(keyword) : switch (target) {
            case title_content -> this.findByTopic(keyword);
            case title -> this.findByTopicTitle(keyword);
            default -> this.findByTopicContent(keyword);
        };
    }

    @Transactional
    public Long addPostComment(Long postId, User user, String postCommentContent) {
        Post post = postRepository.findById(postId).orElse(null);
        PostComment postComment = PostComment.makePostComment(post, user, postCommentContent);
        return  postCommentRepository.save(postComment).getPostCommentId();
    }

    @Transactional
    public void deletePostComment(Long postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId).orElse(null);
        postCommentRepository.delete(postComment);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        Long postContentId = post.getPostContent().getPostContentId();
        likePostRepository.deleteByPostId(postId);
        dislikePostRepository.deleteByPostId(postId);
        recentPostRepository.deleteByPostId(postId);
        postCommentRepository.deleteByPostId(postId);
        postImageRepository.deleteByPostContentId(postContentId);
        postRepository.deletePost(post);
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public TempPost findByUser(User user){
        List<TempPost> postList = tempPostRepository.findByUserUserId(user.getUserId());
        if(postList.isEmpty()) return null;
        return postList.get(0);
    }

    public List<Post> findAboutTopic(String category){
        return postRepository.findByCategoryCategoryContent(category);
    }

    public List<PostComment> findCommentByPost(Long postId){
        return postCommentRepository.findByPostId(postId);
    }

    public List<PostImage> findImageByPost(Long postId){
        return postImageRepository.findByPostId(postId);
    }

    @Transactional
    public Long processAddPost(User user, String category, PostUpload postUpload) {
        Post post = Post.makePost(new PostDto(postUpload.getTitle(),
                postUpload.getContent(),
                this.findCategory(category)), user);
        Long postId = savePost(post);
        this.saveImage(postUpload.getFileList(), post);
        return postId;
    }

    @Transactional
    public void processUpdatePost(Long postId, PostUploadUpdate postUpload) {
        Post post = this.findById(postId);
        this.updatePost(post, postUpload);
        this.updatePostImage(post.getPostContent().getPostContentId(), postUpload.getDeletePaths());
        saveImage(postUpload.getFileList(), post);
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

    @Transactional
    public void saveCategory(Category category){
//        postRepository.save(category);
    }

    private Long savePost(Post post){
        return postRepository.save(post);
    }

    private void savePostImage(PostImage postImage) {
        postImageRepository.save(postImage);
    }

    private List<Post> findByTopic(String topic){
        return postRepository.findByTopic(topic);
    }

    private List<Post> findByTopicTitle(String topic){
        return postRepository.findByTopicTitle(topic);
    }

    private List<Post> findByTopicContent(String topic){
        return postRepository.findByTopicContent(topic);
    }

    private void checkCountAdd(Post post) {
        post.checkCount();
    }

    private void addLikeCount(Post post, User user) {
        List<LikePost> likePost = likePostRepository.findByUserUserIdPostPostId(user.getUserId(), post.getPostId());
        List<DislikePost> dislikePost = dislikePostRepository.findByUserUserIdPostPostId(user.getUserId(), post.getPostId());

        if(likePost.isEmpty() && dislikePost.isEmpty()){
            post.addLike();
            likePostRepository.save(LikePost.makeLikePost(post, user));
            return;
        }else if(likePost.isEmpty()){
            post.addLike();
            dislikePostRepository.delete(dislikePost.get(0));
            return;
        }

        post.subLike();
        likePostRepository.delete(likePost.get(0));
    }

    private void subLikeCount(Post post, User user){
        List<DislikePost> dislikePost = dislikePostRepository.findByUserUserIdPostPostId(user.getUserId(), post.getPostId());
        List<LikePost> likePost = likePostRepository.findByUserUserIdPostPostId(user.getUserId(), post.getPostId());

        if(dislikePost.isEmpty() && likePost.isEmpty()){
            post.subLike();
            dislikePostRepository.save(DislikePost.makeDislikePost(post, user));
            return;
        }else if(dislikePost.isEmpty()){
            post.subLike();
            likePostRepository.delete(likePost.get(0));
            return;
        }

        post.addLike();
        dislikePostRepository.delete(dislikePost.get(0));
    }

    private RecentPost getRecentPostAlreadySeen(Post post, User user) {
        List<RecentPost> recentPost =recentPostRepository.findByUserUserIdPostPostId(user.getUserId(), post.getPostId());
        if(recentPost.isEmpty()){
            return null;
        }
        return recentPost.get(0);
    }

    private void addRecentPost(Post post, User user) {
        RecentPost recentPost = RecentPost.makeRecentPost(post, user);
        recentPostRepository.save(recentPost);
    }

    private void updateRecentPost(RecentPost recentPost){
        recentPost.setRecentPostDate();
    }

    private List<Post> findAboutTopicCount(String stock) {
        return postRepository.findByTopicTitle(stock);
    }

    private List<Post> findAboutTopicLike(String stock) {
        return postRepository.findByTopicTitle(stock);
    }

    private List<Post> findAboutTopicDate(String stock) {
        return postRepository.findByTopicTitle(stock);
    }

    private void updatePost(Post post, PostUploadUpdate postUpload) {
        post.updatePost(postUpload);
    }

    private void updatePostImage(Long postContentId, List<String> imagePath) {
        postImageRepository.deleteByPostContentIdAndImagePathIn(postContentId, imagePath);
    }


    private void saveTempPost(TempPost tempPost) {
        tempPostRepository.save(tempPost);
    }

    private Category findCategory(String category) {
        return categoryRepository.findByCategoryContent(category);
    }

    private void deleteTempPost(TempPost tempPost){
        tempPostRepository.delete(tempPost);
    }

    private void processRecentPost(Post post, User user) {
        RecentPost recentPost = this.getRecentPostAlreadySeen(post, user);
        if(recentPost == null) {
            this.addRecentPost(post, user);
            return;
        }
        this.updateRecentPost(recentPost);
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
}
