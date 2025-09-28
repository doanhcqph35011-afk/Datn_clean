package com.example.Datn_clean.Service;

/**
 * Product Service - Xử lý logic nghiệp vụ sản phẩm
 * @author linhntdph49844
 * @version 1.0
 * @since 2025-01-14
 */
import com.example.Datn_clean.Entity.Product;
import com.example.Datn_clean.Entity.Category;
import com.example.Datn_clean.Repository.ProductRepository;
import com.example.Datn_clean.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findByStatus(true);
    }
    
    // Lấy sản phẩm theo ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    // Lấy sản phẩm theo danh mục
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }
    
    // Tìm kiếm sản phẩm
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }
    
    // Lấy sản phẩm với bộ lọc
    public Page<Product> getProductsWithFilters(
            Long categoryId,
            String brand,
            String gender,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int page,
            int size,
            String sortBy,
            String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return productRepository.findProductsWithFilters(
            categoryId, brand, gender, minPrice, maxPrice, pageable);
    }
    
    // Lấy sản phẩm mới nhất
    public List<Product> getLatestProducts() {
        return productRepository.findTop10ByStatusTrueOrderByCreatedAtDesc();
    }
    
    // Lấy tất cả danh mục
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    // Lấy danh mục theo ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    
    // Lưu sản phẩm
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    // Lưu danh mục
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    
    // Xóa sản phẩm
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    // Đếm sản phẩm theo danh mục
    public Long countProductsByCategory(Long categoryId) {
        return productRepository.countByCategoryCategoryId(categoryId);
    }
}
