package com.newstock.post.controller;

import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.PostComment;
import com.newstock.post.domain.post.PostImage;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.PostDetailDto;
import com.newstock.post.dto.PostDto;
import com.newstock.post.dto.PostUpload;
import com.newstock.post.repository.file.FileStore;
import com.newstock.post.repository.file.UploadFile;
import com.newstock.post.service.PostService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final FileStore fileStore;

    @GetMapping("/post/add/{category}")
    public String viewAddPost(@Login User user, @PathVariable("category") String category, Model model){
        model.addAttribute("category", category);
        return "newpostpage";
    }

    @PostMapping("/post/add/{category}")
    public String addPost(@Login User user,
                          @ModelAttribute("postItem") PostUpload postUpload,
                          BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "newpostpage";
        }

        // 기본적인 post 저장
        PostDto postDto = new PostDto();
        postDto.setTitle(postUpload.getTitle());
        postDto.setPostContent(postUpload.getContent());
        postDto.setCategory("stock");
        Post post = Post.makePost(postDto, user);
        Long postId = postService.savePost(post);

        // 실제 파일 저장
        List<UploadFile> uploadFiles = fileStore.storeFiles(postUpload.getFileList());

        //데이터베이스에 파일 이름 저장
        for(UploadFile uploadFile: uploadFiles){
            PostImage postImage = PostImage.makePostImage(uploadFile.getFilePath(), post.getPostContent());
            postService.savePostImage(postImage);
        }

        return "redirect:/post/" + postId;
    }

    @GetMapping("/post/stock")
    public String viewPost(Model model){
        model.addAttribute("postList", postService.findAll());
        model.addAttribute("mainName", "주식");
        model.addAttribute("category", "stock");
        return "postpage";
    }

    @GetMapping("/post/{postId}")
    public String viewPostDetail(@Login User user,
                                 @PathVariable("postId") Long postId,
                                 Model model){
        Post post = postService.findById(postId);
        List<PostComment> postCommentList = postService.findCommentByPost(postId);
        List<PostImage> postImageList = postService.findImageByPost(postId);
        for(PostImage p : postImageList){
            log.info("sss = {}", p.getPostImagePath());
        }
        model.addAttribute("content",PostDetailDto.makePostDetailDto(post));
        model.addAttribute("postCommentList", postCommentList);
        model.addAttribute("postImageList", postImageList);
        model.addAttribute("viewUser", user);
        return "postdetailpage";
    }

}
