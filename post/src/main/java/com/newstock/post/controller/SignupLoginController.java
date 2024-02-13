package com.newstock.post.controller;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.service.NewsService;
import com.newstock.post.web.SessionConst;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.auth.LoginDto;
import com.newstock.post.dto.auth.SignupDto;
import com.newstock.post.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupLoginController {

    private final UserService userService;
    private final NewsService newsService;

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("signupData") SignupDto signupDto,
                         BindingResult bindingResult){
        if(!signupDto.getId().matches("^[a-zA-Z0-9]+$")){
            bindingResult.reject("idValidate", "아이디는 영어와 숫자로 구성 되어야 합니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "signuppage";
        }

        User user = User.makeuser(signupDto);
        Long userId = userService.save(user);

        for(String interestWord: signupDto.getInterestWord().split(",")){
            userService.savePreferenceTitle(userId, interestWord.trim());
        }

        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String viewSignupPage(){
        return "signuppage";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginData") LoginDto loginDto,
                        BindingResult bindingResult,
                        HttpServletRequest request){

        User user = userService.findByUserId(loginDto.getId(), loginDto.getPassword());

        if(user == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "loginpage";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, user);

        List<PreferenceTitle> userPreferenceTitle = userService.findUserPreferenceTitle(user.getUserId());
        for(PreferenceTitle preferenceTitle: userPreferenceTitle){
            newsService.getNewsData(preferenceTitle.getPreferenceTitle());
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        return "loginpage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
