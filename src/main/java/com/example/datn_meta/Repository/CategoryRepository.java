package com.example.datn_meta.Repository;

import com.example.datn_meta.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Tìm danh mục theo tên
    Category findByCategoryName(String categoryName);
    
    // Tìm danh mục theo tên (tìm kiếm gần đúng)
    List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);
}






