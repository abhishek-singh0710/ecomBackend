package com.ecommerceProject.springboot_ecommerceProject.project.util;

import com.ecommerceProject.springboot_ecommerceProject.project.model.User;
import com.ecommerceProject.springboot_ecommerceProject.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String loggedInEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow( () -> new UsernameNotFoundException("User Not Found With Username "+authentication.getName()));

        return user.getEmail();
    }

    public Long loggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow( () -> new UsernameNotFoundException("User Not Found With Username "+authentication.getName()));

        return user.getUserId();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserName(authentication.getName())
                .orElseThrow( () -> new UsernameNotFoundException("User Not Found With Username "+authentication.getName()));

        return user;
    }
}
