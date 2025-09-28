package com.example.Datn_clean.Controller;

import com.example.Datn_clean.Entity.OrderItem;
import com.example.Datn_clean.Repository.ProductRepository;
import com.example.Datn_clean.Service.OrderItemService;
import com.example.Datn_clean.Service.OrderService;
import com.example.Datn_clean.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order-items")
public class OrderItemController {
    private final OrderService orderService ;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    // Danh sách order items
    @GetMapping("/list")
    public String listOrderItems(Model model) {
        model.addAttribute("orderItem", orderItemService.fillAll());
        return "OrderItem/list"; // templates/OrderItem/list.html
    }

    // Form thêm
    @GetMapping("/form")
    public String showCreateForm(Model model) {
        model.addAttribute("orderItem", new OrderItem());
        model.addAttribute("orders", orderService.findAll());
        model.addAttribute("products", productService.getAllProducts());
        return "OrderItem/form";  // trỏ đúng tới templates/OrderItem/form.html
    }

    // Lưu
    @PostMapping ("/save")
    public String saveOrderItem(@ModelAttribute("orderItem") OrderItem orderItem) {
        orderItemService.save(orderItem);
        return "redirect:/order-items/list";
    }

    // Form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        OrderItem orderItem = orderItemService.findByid(id);
        model.addAttribute("orderItem", orderItem);
      model.addAttribute("orders", orderService.findAll());
        model.addAttribute("products", productService.getAllProducts());
        return "OrderItem/orderitem-form";
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String deleteOrderItem(@PathVariable Integer id) {
        orderItemService.deleteById(id);
        return "redirect:/order-items/list";
    }
}
