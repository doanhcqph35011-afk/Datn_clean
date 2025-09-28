package com.example.Datn_clean.Repository;

/**
 * Product Repository - Truy cập dữ liệu sản phẩm
 * @author linhntdph49844
 * @version 1.0
 * @since 2025-01-14
 */
import com.example.Datn_clean.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Tìm sản phẩm theo danh mục
    List<Product> findByCategoryCategoryId(Long categoryId);
    
    // Tìm sản phẩm theo trạng thái
    List<Product> findByStatus(Boolean status);
    
    // Tìm sản phẩm theo tên (tìm kiếm gần đúng)
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    
    // Tìm sản phẩm theo thương hiệu
    List<Product> findByBrandContainingIgnoreCase(String brand);
    
    // Tìm sản phẩm theo khoảng giá
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Tìm sản phẩm theo giới tính
    List<Product> findByGender(String gender);
    
    // Tìm sản phẩm còn hàng
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
    
    // Tìm kiếm tổng hợp
    @Query("SELECT p FROM Product p WHERE " +
           "(:categoryId IS NULL OR p.category.categoryId = :categoryId) AND " +
           "(:brand IS NULL OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :brand, '%'))) AND " +
           "(:gender IS NULL OR p.gender = :gender) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "p.status = true")
    Page<Product> findProductsWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("brand") String brand,
            @Param("gender") String gender,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
    
    // Tìm sản phẩm mới nhất
    List<Product> findTop10ByStatusTrueOrderByCreatedAtDesc();
    
    // Đếm sản phẩm theo danh mục
    Long countByCategoryCategoryId(Long categoryId);
}
