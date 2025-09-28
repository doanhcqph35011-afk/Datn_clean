package com.example.datn_meta.Controller;

import com.example.datn_meta.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MyController {

    private final ProductService productService;

    // Trang chủ sau khi login thành công
    @GetMapping("/homePage")
    public String homePage(Model model, Principal principal) {
        // principal.getName() = username đang đăng nhập
        model.addAttribute("username", principal.getName());
        
        // Lấy sản phẩm mới nhất để hiển thị trên trang chủ
        model.addAttribute("latestProducts", productService.getLatestProducts());
        
        return "App/homePage"; // resources/templates/Home.html
    }
    
    // Trang chủ công khai (không cần đăng nhập)
    @GetMapping("/")
    public String publicHome(Model model) {
        // Lấy sản phẩm mới nhất để hiển thị trên trang chủ
        model.addAttribute("latestProducts", productService.getLatestProducts());
        return "App/homePage";
    }
}
