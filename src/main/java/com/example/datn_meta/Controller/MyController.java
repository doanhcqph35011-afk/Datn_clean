package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MyController {

    private final UserDao userDao;


    // Trang chủ sau khi login thành công
    @GetMapping("/homePage")
    public String homePage(Model model, Principal principal) {
        // Hiển thị tên người dùng tương tự trang products
        String displayName = null;
        if (principal != null) {
            String name = principal.getName();
            Users user = userDao.findByEmail(name).or(() -> userDao.findByPhoneNumber(name)).orElse(null);
            if (user != null) {
                displayName = (user.getFullName() != null && !user.getFullName().isBlank()) ? user.getFullName() : name;
            } else {
                displayName = name; // fallback email/phone
            }
        }
        model.addAttribute("username", displayName);
        return "App/homePage"; // resources/templates/Home.html
    }

    // Điều hướng root về trang chủ
    @GetMapping("/")
    public String root() {
        return "redirect:/homePage";
    }
}
