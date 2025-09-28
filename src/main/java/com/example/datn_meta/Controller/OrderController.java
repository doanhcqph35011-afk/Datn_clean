package com.example.Datn_clean.Controller;

import com.example.Datn_clean.Entity.order;
import com.example.Datn_clean.Service.CustomerService;
import com.example.Datn_clean.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private  final OrderService orderService;
        private final CustomerService customerService;
    // danh sách đơn  Hàng
    @GetMapping("/list")
    public String listOrders(Model model) {
        List<order> orders = orderService.findAll();
        model.addAttribute("orders",orders);
        return "Order/list";
    }
        // Xem Chi Tiết Đơn Hàng
    @GetMapping("/detail/{id}")
    public String orderDetail(@PathVariable Integer id, Model model) {
        order order1 = orderService.findById(id);
        model.addAttribute("order", order1);
        return "Order/form";
    }
    @GetMapping("/new")
    public String showcreateForm(Model model) {
        model.addAttribute("order", new order()); // đúng tên
        model.addAttribute("customers", customerService.findAll());
        return "Order/form";
    }
        // Lưu Đơn Hàng
        @PostMapping ("/save")
        public String saveOrder(@ModelAttribute("order") order order1) {
            orderService.save(order1); // gọi service lưu xuống DB
            return "redirect:/orders/list"; // quay về trang danh sách
        }
    // Chỉnh sửa đơn Hàng
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        order orderr = orderService.findById(id);
        model.addAttribute("order", orderr);
        model.addAttribute("customers", customerService.findAll());
        return "Order/form";
    }
    // Xóa
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteById(id);
        return "redirect:/orders/list";
    }
}
