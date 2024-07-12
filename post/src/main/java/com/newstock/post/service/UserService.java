package com.newstock.post.service;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.auth.SignupDto;
import com.newstock.post.repository.user.PreferenceTitleRepository;
import com.newstock.post.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
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
        User user = User.makeuser(signupDto, bCryptPasswordEncoder);
        save(user);
        for(String interestWord: signupDto.getInterestWord().split(",")){
            savePreferenceTitle(user, interestWord.trim());
        }
    }

    public List<String> processLogin(HttpServletRequest request, User user) {
        return this.findUserPreferenceTitle(user.getUserId()).stream()
                .map(PreferenceTitle::getPreferenceTitle)
                .collect(Collectors.toList());
    }

    public User findByUserId(String userId){
        List<User> temp = userRepository.findById(userId);
        if(temp.isEmpty()) return null;
        return temp.get(0);
    }

    public List<PreferenceTitle> findUserPreferenceTitle(Long userId){
        return userRepository.findUserPreferenceTitle(userId);
    }

    @Transactional
    public void addPreferenceTitle(String interestWord, Long userId) {
        PreferenceTitle preferenceTitle = PreferenceTitle.preferenceTitle(userRepository.findById(userId).orElse(null), interestWord);
        preferenceTitleRepository.save(preferenceTitle);
    }

    @Transactional
    public void deletePreferenceTitle(Long preferenceTitleId) {
        PreferenceTitle preferenceTitle = preferenceTitleRepository.findById(preferenceTitleId).orElse(null);
        if(preferenceTitle == null) return;
        preferenceTitleRepository.delete(preferenceTitle);
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

    public void savePreferenceTitle(User user, String preferenceWord){
        String removeSpace = preferenceWord.replaceAll(" ", "");
        if(removeSpace.isEmpty() || removeSpace.equals(","))return;
        if(removeSpace.startsWith(",")) removeSpace = removeSpace.substring(1);
        if(removeSpace.endsWith(",")) removeSpace = removeSpace.substring(0, removeSpace.length()-1);
        Arrays.stream(removeSpace.split(","))
                        .forEach(s -> preferenceTitleRepository
                                .save(PreferenceTitle.preferenceTitle(user, s)));
    }

    @Transactional
    public Long save(User user){
        User userS = userRepository.save(user);
        return userRepository.save(user).getUserId();
    }

    private boolean checkIdDuplicate(String id) {
        return userRepository.existsById(id);
    }
}
