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
    public String account(Model model, Principal principal) {
        // lấy thông tin user bằng email hoặc sdt


       String loginId = principal.getName();
        // tìm user có thông tin như vậy

        Users user = userDao.findByEmailOrPhone(loginId).orElse(null);
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "App/account";
    }
}
