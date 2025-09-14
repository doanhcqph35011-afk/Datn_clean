package com.example.datn_meta.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MyController {


    // Trang chủ sau khi login thành công
    @GetMapping("/homePage")
    public String homePage(Model model, Principal principal) {
        // principal.getName() = username đang đăng nhập
        model.addAttribute("username", principal.getName());
        return "App/homePage"; // resources/templates/Home.html
    }
}
