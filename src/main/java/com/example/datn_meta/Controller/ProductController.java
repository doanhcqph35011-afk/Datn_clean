package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.Product;
import com.example.datn_meta.Entity.Category;
import com.example.datn_meta.Entity.Users;
import com.example.datn_meta.Repository.UserDao;
import com.example.datn_meta.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserDao userDao;

    @GetMapping
    public String productsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String search,
            Model model,
            Principal principal) {

        // Hiển thị tên người dùng nếu đã đăng nhập
        if (principal != null) {
            Optional<Users> userOpt = userDao.findByEmail(principal.getName())
                    .or(() -> userDao.findByPhoneNumber(principal.getName()));

            if (userOpt.isPresent()) {
                Users user = userOpt.get();
                String displayName = user.getFullName() != null ? user.getFullName() : principal.getName();
                model.addAttribute("username", displayName);
            }
        }

        Page<Product> products = null;

        if (search != null && !search.trim().isEmpty()) {
            // Tìm kiếm theo từ khóa
            List<Product> searchResults = productService.searchProducts(search);
            model.addAttribute("products", searchResults);
            model.addAttribute("search", search);
        } else {
            // Lọc sản phẩm
            products = productService.getProductsWithFilters(
                    categoryId, brand, gender, minPrice, maxPrice, page, size, sortBy, sortDir);
            model.addAttribute("products", products);
        }

        // Lấy danh sách danh mục
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("categories", categories);

        // Thông tin phân trang
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products != null ? products.getTotalPages() : 0);
        model.addAttribute("totalElements", products != null ? products.getTotalElements() : 0);

        // Thông tin bộ lọc
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedBrand", brand);
        model.addAttribute("selectedGender", gender);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "products/products";
    }

    // Trang chi tiết sản phẩm
    // @author linhntdph49844
    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        model.addAttribute("product", product);

        // Lấy sản phẩm liên quan (cùng danh mục)
        List<Product> relatedProducts = productService.getProductsByCategory(product.getCategory().getCategoryId());
        relatedProducts.removeIf(p -> p.getProductId().equals(id)); // Loại bỏ sản phẩm hiện tại
        if (relatedProducts.size() > 4) {
            relatedProducts = relatedProducts.subList(0, 4);
        }
        model.addAttribute("relatedProducts", relatedProducts);

        return "products/product-detail";
    }

    // Trang sản phẩm theo danh mục
    @GetMapping("/category/{categoryId}")
    public String productsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Category category = productService.getCategoryById(categoryId)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        Page<Product> products = productService.getProductsWithFilters(
                categoryId, null, null, null, null, page, size, "createdAt", "desc");

        model.addAttribute("products", products);
        model.addAttribute("category", category);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalElements", products.getTotalElements());

        return "products/products-by-category";
    }

    // API tìm kiếm sản phẩm (AJAX)
    @GetMapping("/search")
    @ResponseBody
    public List<Product> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    // API lấy sản phẩm mới nhất
    @GetMapping("/latest")
    @ResponseBody
    public List<Product> getLatestProducts() {
        return productService.getLatestProducts();
    }

    // API tìm kiếm theo khoảng giá
    @GetMapping("/search-by-price")
    @ResponseBody
    public List<Product> searchByPrice(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        return productService.searchByPriceRange(minPrice, maxPrice);
    }
}
