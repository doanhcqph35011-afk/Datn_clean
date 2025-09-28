package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.Customer;
import com.example.datn_meta.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping ("/customers")
public class CustomerController {
    private final CustomerService customerService;

    // Danh sách khách hàng
    @GetMapping("/list")
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "Customer/list"; // templates/Customer/list.html
    }

    // Form thêm
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "Customer/form";
    }

    // Lưu
    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.save(customer);
        return "redirect:/customers/list";
    }

    // Form sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        return "Customer/form";
    }

    // Xóa
    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id);
        return "redirect:/customers/list";
    }

}
