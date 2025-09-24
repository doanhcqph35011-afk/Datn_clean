package com.example.datn_meta.Controller;
import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserDao userDao;
    // Truy cập trang quản trị
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        String loginId = principal.getName();
        Users user = userDao.findByEmailOrPhone(loginId).orElse(null);
        if (user != null) {
            model.addAttribute("fullName", user.getFullName());
        }
        return "admin/dashboard";
    }
    @GetMapping({"/user", "/user-info"})
    public String userInfo(Model model, Principal principal) {
        String loginId = principal.getName();
        Users user = userDao.findByEmailOrPhone(loginId).orElse(null);
        if (user != null) {
            model.addAttribute("fullName", user.getFullName());
        }
        // Lấy toàn bộ danh sách user
        List<Users> allUsers = userDao.findAll();
        model.addAttribute("users", allUsers);
        return "admin/user";
    }

    // ✅ Mở form edit
    @GetMapping("/user/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        Users user = userDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));
        model.addAttribute("user", user);
        return "admin/user-edit"; // templates/admin/user-edit.html
    }
    // ✅ Submit form edit (cập nhật)
    @PutMapping("/user/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") Users form) {
        form.setUserId(id);        // đảm bảo set id trước khi save
        userDao.save(form);
        return "redirect:/admin/user?updated=" + id;
    }

    // ✅ Xoá (dùng POST cho đơn giản; khỏi cấu hình HiddenHttpMethod)
    @DeleteMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userDao.deleteById(id);
        return "redirect:/admin/user?deleted=" + id;
    }
}


