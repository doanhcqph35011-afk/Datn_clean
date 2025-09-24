package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
@Controller
public class AccountController {
    @Autowired
    UserDao userDao;

    @GetMapping("/account")
    public String account(Model model, Authentication authentication) {
        // LẤY username từ principal (có thể là email HOẶC phone)
        String loginId = null;
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            loginId = ud.getUsername();
        } else if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User oau) {
            loginId = (String) oau.getAttribute("email"); // OAuth2 thì là email
        }
        // ⚠️ ĐỔI Ở ĐÂY: tìm theo email **hoặc** phone (trước đây bạn chỉ findByEmail)
        Users user = (loginId == null) ? null
                : userDao.findByEmailOrPhone(loginId).orElse(null);

        if (user != null) {
            model.addAttribute("user", user);                // object đầy đủ
            model.addAttribute("fullName", user.getFullName()); // tên để header dùng
        } else {
            model.addAttribute("fullName", null); // tránh SpEL lỗi
        }
        return "App/account"; // templates/App/account.html
    }

}
