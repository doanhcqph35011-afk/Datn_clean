package com.example.Datn_clean.Controller;


import com.example.Datn_clean.Entity.Employee;
import com.example.Datn_clean.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    // Danh sách nhân viên
    @GetMapping("/danhsachnhanvien")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "redirect:/employees"; // templates/Employee/list.html
    }

    // Form thêm nhân viên
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "Employee/form"; // templates/Employee/form.html
    }

    // Lưu nhân viên
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee) {
        employeeService.save(employee);
        return "redirect:/employees";
    }

    // Form sửa nhân viên
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable  Long id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        return "Employee/form";
    }

    // Xóa nhân viên
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return "redirect:/employees";
    }
}
