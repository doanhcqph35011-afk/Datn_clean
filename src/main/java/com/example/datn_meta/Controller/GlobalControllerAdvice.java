package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserDao userDao;

    @ModelAttribute
    public void addUserInfo(Model model, Principal principal) {
        if (principal != null) {
            String loginId = principal.getName();
            Users user = userDao.findByEmailOrPhone(loginId).orElse(null);
            if (user != null) {
                model.addAttribute("currentUser", user);
                model.addAttribute("Fullname", user.getFullName());
            }
        }
    }
}
