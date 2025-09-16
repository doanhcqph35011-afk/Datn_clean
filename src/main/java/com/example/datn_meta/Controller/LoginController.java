package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.RegisterRequest;
import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import com.example.datn_meta.Service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    @Autowired
     AuthenticationManager authenticationManager;
    @Autowired
    JWTService jwtService;
    @Autowired
    UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder;

    // Hiển thị form login
    @GetMapping("/login")
    public String showLoginForm() {
        return "Auth/login"; // => templates/Auth/login.html
    }
    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Users());
        return "Auth/register"; // templates/Auth/register.html
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String register(@ModelAttribute("user") Users user,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("confirmPassword") String confirmPassword) {

        // Check confirmPassword
        if (user.getPassword() == null || !user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu nhập lại không khớp!");
            return "redirect:/auth/register";
        }

        // Nếu email đã tồn tại
        if (user.getEmail() != null && userDao.findByEmail(user.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được đăng ký!");
            return "redirect:/auth/register";
        }

        // Nếu SĐT đã tồn tại
        if (user.getPhoneNumber() != null && userDao.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã được đăng ký!");
            return "redirect:/auth/register";
        }

        // Nếu cả email và phone đều null
        if (user.getEmail() == null && user.getPhoneNumber() == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn phải nhập Email hoặc SĐT!");
            return "redirect:/auth/register";
        }

        // Thiết lập giá trị mặc định
        user.setRole(Users.Role.USER);
        user.setProvider("local");
        user.setCreatedAt(LocalDateTime.now());

        // Gán provider_id duy nhất cho local user
        if (user.getProvider().equals("local")) {
            user.setProviderId(user.getEmail() != null ? user.getEmail() : user.getPhoneNumber());
        }

        try {
            userDao.save(user);
            redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu user: " + e.getMessage());
            return "redirect:/auth/register";
        }
    }

    // Xử lý login
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes) {
        Optional<Users> userOpt;

        // Check login bằng email hoặc phone
        if (username.contains("@")) {
            userOpt = userDao.findByEmail(username);
        } else {
            userOpt = userDao.findByPhoneNumber(username);
        }

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tài khoản không tồn tại!");
            return "redirect:/auth/login";
        }

        Users user = userOpt.get();

        // So sánh mật khẩu (không mã hóa)
        if (!password.equals(user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Sai mật khẩu!");
            return "redirect:/auth/login";
        }

        // Thành công
        redirectAttributes.addFlashAttribute("username", user.getFullName());
        return "redirect:/homePage";
    }

    @GetMapping("/oauth2/success")
    public String oauth2success(Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Lấy thông tin cơ bản từ OAuth2 provider
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub"); // Google dùng "sub"
        if (providerId == null) {
            providerId = oAuth2User.getAttribute("id"); // Facebook dùng "id"
        }

        // Xác định provider (google / facebook)
        String provider = "google";
        if (oAuth2User.getAttribute("picture") == null && oAuth2User.getAttribute("id") != null) {
            provider = "facebook";
        }

        // Kiểm tra user trong DB
        Optional<Users> existingUser = userDao.findByEmail(email);

        Users user;
        if (existingUser.isPresent()) {
            // Nếu đã có user → update thông tin
            user = existingUser.get();
            user.setFullName(name != null ? name : user.getFullName());
            user.setProvider(provider);
            user.setProviderId(providerId);
        } else {
            // Nếu chưa có → tạo mới
            user = new Users();
            user.setFullName(name != null ? name : "No Name");
            user.setEmail(email);
            user.setPassword("oauth2"); // dummy password
            user.setProvider(provider);
            user.setProviderId(providerId);
            user.setRole(Users.Role.USER);
            user.setCreatedAt(LocalDateTime.now());
        }

        // Save hoặc update user
        userDao.save(user);

        // Gửi username ra flash attribute
        redirectAttributes.addFlashAttribute("username", user.getFullName());
        return "redirect:/homePage";
    }
}
