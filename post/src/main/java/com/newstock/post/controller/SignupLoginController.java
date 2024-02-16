package com.newstock.post.controller;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.service.NewsService;
import com.newstock.post.web.Login;
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
import org.springframework.ui.Model;
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
        userService.validateLoginData(signupDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signuppage";
        }
        userService.processSignup(signupDto);
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String viewSignupPage(Model model){
        model.addAttribute("signupData", new SignupDto());
        return "signuppage";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginData") LoginDto loginDto,
                        BindingResult bindingResult,
                        HttpServletRequest request){
        User user = userService.findByUserId(loginDto.getId(), loginDto.getPassword());
        if(user == null){
            bindingResult.rejectValue("id", "idFail");
            return "loginpage";
        }
        userService.processLogin(request,user)
                .forEach(newsService::getNewsData);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String viewLoginPage(Model model){
        model.addAttribute("loginData", new LoginDto());
        return "loginpage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        userService.removeSession(request);
        return "redirect:/";
    }
}
