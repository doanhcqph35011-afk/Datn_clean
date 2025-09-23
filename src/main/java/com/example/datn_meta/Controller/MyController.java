package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller

public class MyController {
    // Dung cho trường hợp login bằng email hay gg fb thì đều có thể mapping tới các đường dẫn khác k bị lỗi mapping
    @Autowired
    private UserDao userDao;

    // Trang chủ sau khi login thành công
    @GetMapping({"/", "/homePage"})
    public String homePage(Model model, Principal principal) {
//        model.addAttribute("username", principal.getName());
        String loginId = principal.getName();
        Users user = userDao.findByEmailOrPhone(loginId).orElse(null);

        if (user != null) {
            model.addAttribute("fullname", user.getFullName());
        }
        return "App/homePage";
    }
    // Truy cập trang quản trị
    @GetMapping("admin/dashboard")
    public String dashboard(Model model, Principal principal) {
        String loginId = principal.getName();
        Users user = userDao.findByEmailOrPhone(loginId).orElse(null);
        if (user != null) {
            model.addAttribute("fullname", user.getFullName());
        }
        return "admin/dashboard";
    }
    @GetMapping("/employee/staff")
    public String dashStaff(Model model, Principal principal) {
        String loginId = principal.getName();
        Users user = userDao.findByEmailOrPhone(loginId).orElse(null);
        if (user != null) {
            model.addAttribute("fullname", user.getFullName());
        }
        return "employee/staff";
    }

}
