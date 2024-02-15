package com.newstock.post.controller;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.news.News;
import com.newstock.post.domain.post.*;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.post.PostDetailDto;
import com.newstock.post.dto.post.PostDto;
import com.newstock.post.dto.post.PostUpload;
import com.newstock.post.dto.post.PostUploadUpdate;
import com.newstock.post.repository.file.FileStore;
import com.newstock.post.repository.file.UploadFile;
import com.newstock.post.service.PostService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final FileStore fileStore;

    @GetMapping("/post/add/{category}")
    public String viewAddPost(@Login User user, @PathVariable("category") String category, @ModelAttribute("tempData") TempPost tempData ,Model model){
        model.addAttribute("tempData", tempData);
        model.addAttribute("category", category);
        return "newpostpage";
    }

    @PostMapping("/post/add/{category}")
    public String addPost(@Login User user,
                          @PathVariable("category") String category,
                          @Validated @ModelAttribute("postItem") PostUpload postUpload,
                          BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "newpostpage";
        }

        // 기본적인 post 저장
        PostDto postDto = new PostDto();
        postDto.setTitle(postUpload.getTitle());
        postDto.setPostContent(postUpload.getContent());
        postDto.setCategory(postService.findCategory(category));
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
    public String viewPostStockBoard(@RequestParam(value = "target", required = false) String target,
                                     Model model){

        List<Post> postList = postService.findAboutTopic("stock");;

        Comparator<Post> postComparator;

        if(target != null) {
            postComparator = switch (target) {
                case "count" -> Comparator.comparingInt(Post::getPostCheckCount).reversed();
                case "like" -> Comparator.comparingInt(Post::getPostLikeCount).reversed();
                default -> Comparator.comparing(Post::getPostDate).reversed();
            };
            postList.sort(postComparator);
        }

        model.addAttribute("postList", postList);
        model.addAttribute("mainName", "주식");
        model.addAttribute("category", "stock");
        return "postpage";
    }

    @GetMapping("/post/freeBoard")
    public String viewPostFreeBoard(@RequestParam(value = "target", required = false) String target,
                                    Model model){

        List<Post> postList = postService.findAboutTopic("freeBoard");;

        Comparator<Post> postComparator;

        if(target != null) {
            postComparator = switch (target) {
                case "count" -> Comparator.comparingInt(Post::getPostCheckCount).reversed();
                case "like" -> Comparator.comparingInt(Post::getPostLikeCount).reversed();
                default -> Comparator.comparing(Post::getPostDate).reversed();
            };
            postList.sort(postComparator);
        }

        model.addAttribute("postList", postList);
        model.addAttribute("mainName", "자유");
        model.addAttribute("category", "freeBoard");
        return "postpage";
    }

    @GetMapping("/post/{postId}")
    public String viewPostDetail(@Login User user,
                                 @PathVariable("postId") Long postId,
                                 @ModelAttribute("isLike") String isLike,
                                 Model model){

        Post post = postService.findById(postId);
        List<PostComment> postCommentList = postService.findCommentByPost(postId);
        List<PostImage> postImageList = postService.findImageByPost(postId);

        if(user != null){
            RecentPost recentPost = postService.getRecentPostAlreadySeen(post, user);
            if(recentPost == null){
                postService.addRecentPost(post,user);
            }else{
                postService.updateRecentPost(recentPost);
            }
        }

        if(isLike.isEmpty()) postService.checkCountAdd(post);

        model.addAttribute("content",PostDetailDto.makePostDetailDto(post, user));
        model.addAttribute("category", post.getCategory().getCategoryContent());
        model.addAttribute("postCommentList", postCommentList);
        model.addAttribute("postImageList", postImageList);
        model.addAttribute("viewUser", user);
        return "postdetailpage";
    }

    @PostMapping("/post/{postId}")
    public String postLike(@Login User user,
                           @RequestParam String action,
                           @PathVariable("postId") Long postId,
                           RedirectAttributes redirectAttributes){
        Post post = postService.findById(postId);

        if(action.equals("like")){
            postService.addLikeCount(post, user);
        }else if(action.equals("dislike")){
            postService.subLikeCount(post, user);
        }

        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/add")
    public String postAddComment(@Login User user,
                                 @PathVariable("postId") Long postId,
                                 @RequestParam("postCommentContent") String postCommentContent,
                                 RedirectAttributes redirectAttributes){
        postService.addPostComment(postId, user, postCommentContent);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/delete")
    public String postDeleteComment(@PathVariable("postId") Long postId,
                                 @RequestParam("postCommentId") Long postCommentId,
                                 RedirectAttributes redirectAttributes){
        postService.deletePostComment(postCommentId);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/post/" + postId;
    }

    @GetMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return "redirect:/mypage";
    }

    @GetMapping("/post/update/{postId}")
    public String viewUpdatePost(@PathVariable("postId") Long postId,
                             Model model){
        Post post = postService.findById(postId);
        List<PostImage> postImageList = postService.findImageByPost(postId);

        model.addAttribute("post", post);
        model.addAttribute("postImageList", postImageList);

        return "updatepostpage";
    }

    @PostMapping("/post/update/{postId}")
    public String updatePost(@Login User user,
                             @PathVariable("postId") Long postId,
                             @Validated @ModelAttribute("postItem") PostUploadUpdate postUpload,
                             BindingResult bindingResult){

        Post post = postService.findById(postId);

        if (bindingResult.hasErrors()) {
            log.info("sadfsadf");
            return "redirect:/post/update/" + postId;
        }

        postService.updatePost(post, postUpload);
        postService.updatePostImage(post.getPostContent().getPostContentId(), postUpload.getImagePaths());
        // 실제 파일 저장
        List<UploadFile> uploadFiles = fileStore.storeFiles(postUpload.getFileList());

        //데이터베이스에 파일 이름 저장
        for(UploadFile uploadFile: uploadFiles){
            PostImage postImage = PostImage.makePostImage(uploadFile.getFilePath(), post.getPostContent());
            postService.savePostImage(postImage);
        }

        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/add/{category}/temp")
    public String saveTempPost(@Login User user,
                               @PathVariable("category") String category,
                               @ModelAttribute("postItem") PostUploadUpdate postUpload){

        // 기본적인 post 저장
        PostDto postDto = new PostDto();
        postDto.setTitle(postUpload.getTitle());
        postDto.setPostContent(postUpload.getContent());
        postDto.setCategory(postService.findCategory(category));

        TempPost tempPost = TempPost.tempPost(postDto,user);
        TempPost findTempPost = postService.findByUser(user);

        if(findTempPost != null){
            postService.deleteTempPost(findTempPost);
        }

        postService.saveTempPost(tempPost);
        return "redirect:/post/add/" + category;
    }

    @GetMapping("/post/add/temp/{category}")
    public String viewTempPost(@Login User user,
                               @PathVariable("category") String category,
                               RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("tempData", postService.findByUser(user));
        return "redirect:/post/add/" + category;
    }
}
