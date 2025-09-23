package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserDao userDao;

    @ModelAttribute ("currentUser")
    public void addUserInfo(Model model, Authentication authentication) {
        if (authentication == null) return;

        Object principal = authentication.getPrincipal();
        String email = null;

        if (principal instanceof Users user) {
            email = user.getEmail();
        } else if (principal instanceof OAuth2User oAuth2User) {
            email = oAuth2User.getAttribute("email");

        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
        // Trường hợp login bằng tài khoản/mật khẩu
        email = userDetails.getUsername(); // thường là email hoặc username
        }

        if (email != null) {
            userDao.findByEmail(email).ifPresent(u -> model.addAttribute("fullName", u.getFullName()));

        }
    }
}

