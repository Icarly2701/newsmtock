package com.newstock.post.service;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.repository.PreferenceTitleRepository;
import com.newstock.post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PreferenceTitleRepository preferenceTitleRepository;

    public Long save(User user){
        return userRepository.save(user);
    }

    public void savePreferenceTitle(Long userId, String preferenceWord){
        User user = userRepository.findById(userId);
        PreferenceTitle preferenceTitle = PreferenceTitle.preferenceTitle(user, preferenceWord);
        preferenceTitleRepository.save(preferenceTitle);
    }

    public User findByUserId(String userId, String userPassword){

        List<User> temp = userRepository.findByUserId(userId);
        if(temp.isEmpty()) return null;

        User user = temp.get(0);
        if(!user.getPassword().equals(userPassword)) return null;
        return user;
    }

    public List<PreferenceTitle> findUserPreferenceTitle(Long userId){
        return userRepository.findUserPreferenceTitle(userId);
    }

    @Transactional
    public void deletePreferenceTitle(Long preferenceTitleId) {
        preferenceTitleRepository.delete(preferenceTitleRepository.findById(preferenceTitleId));
    }
}
