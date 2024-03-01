package com.newstock.post.service;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.auth.SignupDto;
import com.newstock.post.repository.user.PreferenceTitleRepository;
import com.newstock.post.repository.user.UserRepository;
import com.newstock.post.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PreferenceTitleRepository preferenceTitleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void processSignup(SignupDto signupDto) {
        Long userId = this.save(User.makeuser(signupDto, bCryptPasswordEncoder));
        for(String interestWord: signupDto.getInterestWord().split(",")){
            this.savePreferenceTitle(userId, interestWord.trim());
        }
    }

    public List<String> processLogin(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, user);
        return this.findUserPreferenceTitle(user.getUserId()).stream()
                .map(PreferenceTitle::getPreferenceTitle)
                .collect(Collectors.toList());
    }

    public User findByUserId(String userId){
        List<User> temp = userRepository.findByUserId(userId);
        if(temp.isEmpty()) return null;
        return temp.get(0);
    }

    public List<PreferenceTitle> findUserPreferenceTitle(Long userId){
        return userRepository.findUserPreferenceTitle(userId);
    }

    @Transactional
    public void addPreferenceTitle(String interestWord, Long userId) {
        PreferenceTitle preferenceTitle = PreferenceTitle.preferenceTitle(userRepository.findById(userId), interestWord);
        preferenceTitleRepository.save(preferenceTitle);
    }

    @Transactional
    public void deletePreferenceTitle(Long preferenceTitleId) {
        preferenceTitleRepository.delete(preferenceTitleRepository.findById(preferenceTitleId));
    }

    public void validateLoginData(SignupDto signupDto, BindingResult bindingResult) {
        if(!signupDto.getId().matches("^[a-zA-Z0-9]+$")){
            bindingResult.rejectValue("id", "idInvalid");
        }
        if(this.checkIdDuplicate(signupDto.getId())){
            bindingResult.rejectValue("id", "idDuplicate");
        }
    }

    public void removeSession(HttpServletRequest request) {
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public void savePreferenceTitle(Long userId, String preferenceWord){
        if(!preferenceWord.trim().isEmpty()){
            User user = userRepository.findById(userId);
            PreferenceTitle preferenceTitle = PreferenceTitle.preferenceTitle(user, preferenceWord);
            preferenceTitleRepository.save(preferenceTitle);
        }
    }

    public Long save(User user){
        return userRepository.save(user);
    }

    private boolean checkIdDuplicate(String id) {
        return userRepository.existsById(id);
    }
}
