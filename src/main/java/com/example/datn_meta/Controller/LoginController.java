package com.example.Datn_clean.Controller;

import com.example.Datn_clean.Entity.RegisterRequest;
import com.example.Datn_clean.Entity.Users;
import com.example.Datn_clean.Repository.UserDao;
import com.example.Datn_clean.Service.JWTService;
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

//    private Users.Role toRole(String roleStr) {
//        if (roleStr == null || roleStr.isBlank()) return Users.Role.USER; // mặc định
//        try {
//            return Users.Role.valueOf(roleStr.trim().toUpperCase());
//        } catch (IllegalArgumentException ex) {
//            return Users.Role.USER; // giá trị không hợp lệ -> fallback
//        }
//    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Users());
        return "Auth/register"; // => templates/Auth/register.html
    }
    @PostMapping("/register")
    public String register(@ModelAttribute("user") Users user,
                           RedirectAttributes redirectAttributes,
                           @RequestParam("confirmPassword") String confirmPassword) {

        String email = (user.getEmail() != null) ? user.getEmail().trim().toLowerCase() : null;
        String phone = (user.getPhoneNumber() != null) ? user.getPhoneNumber().trim() : null;
        user.setEmail((email != null && !email.isEmpty()) ? email : null);
        user.setPhoneNumber((phone != null && !phone.isEmpty()) ? phone : null);

        // Chuẩn hoá dữ liệu: "" => null
        if (user.getEmail() != null && user.getEmail().isBlank()) {
            user.setEmail(null);
        }
        if (user.getPhoneNumber() != null && user.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(null);
        }
        // Check confirmPassword
        if (user.getPassword() == null || !user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu nhập lại không khớp!");
            return "redirect:/auth/register";
        }

        // Check email tồn tại
        if (user.getEmail() != null && !user.getEmail().isBlank()
                && userDao.findByEmail(user.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Email đã được đăng ký!");
            return "redirect:/auth/register";
        }

        // Check sdt tồn tại
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isBlank()
                && userDao.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Số điện thoại đã được đăng ký!");
            return "redirect:/auth/register";
        }

        // Nếu cả email và sdt đều null → báo lỗi
        if ((user.getEmail() == null || user.getEmail().isBlank()) &&
                (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank())) {
            redirectAttributes.addFlashAttribute("error", "Bạn phải nhập Email hoặc Số điện thoại!");
            return "redirect:/auth/register";
        }

        // FIX: mã hoá mật khẩu
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Users.Role.USER);
        user.setProvider("local");
        user.setCreatedAt(LocalDateTime.now());

        try {
            userDao.save(user);
        } catch (DataIntegrityViolationException ex) {
            // FIX: phòng DB ném lỗi unique index
            redirectAttributes.addFlashAttribute("error", "Email hoặc SĐT đã tồn tại!");
            return "redirect:/auth/register";
        }

        // OK
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/auth/login";

    }

    // Xử lý login
    @PostMapping("/auth/login")
    public String login(String username, String password,
                        RedirectAttributes redirectAttributes) {
        Optional<Users> userOpt;

        // Xác định login bằng email hay phone
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

        // Kiểm tra mật khẩu
        if (!passwordEncoder.matches(password, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Sai mật khẩu!");
            return "redirect:/auth/login";
        }

        // Nếu login thành công → chuyển sang home
        redirectAttributes.addFlashAttribute("username", user.getFullName());
        return "redirect:/homePage";
    }

    @GetMapping("oauth2/success")
    public String oauth2success(Authentication authentication, RedirectAttributes redirectAttributes) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        // Lưu user nếu chưa tồn tại
        userDao.findByEmail(email).orElseGet(() -> {
            Users newUser = new Users();
            newUser.setFullName(name);
            newUser.setEmail(email);
            newUser.setPassword("oauth2"); // mật khẩu dummy
            newUser.setProvider("google");
            newUser.setRole(Users.Role.USER);
            return userDao.save(newUser);
        });
        redirectAttributes.addFlashAttribute("username",name!=null?name:email);
        return "redirect:/homePage";

    }
}
