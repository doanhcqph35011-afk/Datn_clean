package com.example.Datn_clean.Service;

/**
 * Data Initializer - Khởi tạo dữ liệu mẫu
 * @author linhntdph49844
 * @version 1.0
 * @since 2025-01-14
 */
import com.example.Datn_clean.Entity.Category;
import com.example.Datn_clean.Entity.Product;
import com.example.Datn_clean.Entity.ProductImage;
import com.example.Datn_clean.Repository.CategoryRepository;
import com.example.Datn_clean.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Chỉ tạo dữ liệu nếu chưa có
        if (categoryRepository.count() == 0) {
            createSampleData();
        }
    }
    
    private void createSampleData() {
        // Tạo danh mục
        Category ao = new Category();
        ao.setCategoryName("Áo");
        ao = categoryRepository.save(ao);
        
        Category quan = new Category();
        quan.setCategoryName("Quần");
        quan = categoryRepository.save(quan);
        
        Category giay = new Category();
        giay.setCategoryName("Giày");
        giay = categoryRepository.save(giay);
        
        Category phuKien = new Category();
        phuKien.setCategoryName("Phụ kiện");
        phuKien = categoryRepository.save(phuKien);
        
        // Tạo sản phẩm mẫu
        createSampleProducts(ao, quan, giay, phuKien);
    }
    
    private void createSampleProducts(Category ao, Category quan, Category giay, Category phuKien) {
        // Áo sơ mi nam
        Product aoSoMi = new Product();
        aoSoMi.setProductName("Áo sơ mi nam cao cấp");
        aoSoMi.setCategory(ao);
        aoSoMi.setBrand("Aristino");
        aoSoMi.setSize("M");
        aoSoMi.setColor("Trắng");
        aoSoMi.setMaterial("Cotton 100%");
        aoSoMi.setGender("Nam");
        aoSoMi.setPrice(new BigDecimal("850000"));
        aoSoMi.setStockQuantity(50);
        aoSoMi.setDescription("Áo sơ mi nam cao cấp với chất liệu cotton 100%, thiết kế thanh lịch phù hợp cho công sở và các dịp trang trọng.");
        aoSoMi.setStatus(true);
        aoSoMi = productRepository.save(aoSoMi);
        
        // Áo polo
        Product aoPolo = new Product();
        aoPolo.setProductName("Áo Polo nam thể thao");
        aoPolo.setCategory(ao);
        aoPolo.setBrand("Aristino");
        aoPolo.setSize("L");
        aoPolo.setColor("Xanh navy");
        aoPolo.setMaterial("Polyester 65%, Cotton 35%");
        aoPolo.setGender("Nam");
        aoPolo.setPrice(new BigDecimal("650000"));
        aoPolo.setStockQuantity(30);
        aoPolo.setDescription("Áo polo nam thể thao với thiết kế năng động, chất liệu thoáng mát phù hợp cho các hoạt động thể thao.");
        aoPolo.setStatus(true);
        aoPolo = productRepository.save(aoPolo);
        
        // Quần âu
        Product quanAu = new Product();
        quanAu.setProductName("Quần âu nam công sở");
        quanAu.setCategory(quan);
        quanAu.setBrand("Aristino Business");
        quanAu.setSize("32");
        quanAu.setColor("Đen");
        quanAu.setMaterial("Wool 70%, Polyester 30%");
        quanAu.setGender("Nam");
        quanAu.setPrice(new BigDecimal("1200000"));
        quanAu.setStockQuantity(25);
        quanAu.setDescription("Quần âu nam công sở với chất liệu cao cấp, thiết kế thanh lịch phù hợp cho môi trường công sở.");
        quanAu.setStatus(true);
        quanAu = productRepository.save(quanAu);
        
        // Quần jeans
        Product quanJeans = new Product();
        quanJeans.setProductName("Quần jeans nam dáng slim");
        quanJeans.setCategory(quan);
        quanJeans.setBrand("Aristino");
        quanJeans.setSize("30");
        quanJeans.setColor("Xanh đậm");
        quanJeans.setMaterial("Denim 100%");
        quanJeans.setGender("Nam");
        quanJeans.setPrice(new BigDecimal("750000"));
        quanJeans.setStockQuantity(40);
        quanJeans.setDescription("Quần jeans nam dáng slim với thiết kế trẻ trung, chất liệu denim cao cấp.");
        quanJeans.setStatus(true);
        quanJeans = productRepository.save(quanJeans);
        
        // Giày sneaker
        Product giaySneaker = new Product();
        giaySneaker.setProductName("Giày sneaker nam thể thao");
        giaySneaker.setCategory(giay);
        giaySneaker.setBrand("Aristino");
        giaySneaker.setSize("42");
        giaySneaker.setColor("Trắng");
        giaySneaker.setMaterial("Da tổng hợp");
        giaySneaker.setGender("Nam");
        giaySneaker.setPrice(new BigDecimal("1500000"));
        giaySneaker.setStockQuantity(20);
        giaySneaker.setDescription("Giày sneaker nam thể thao với thiết kế hiện đại, đế êm ái phù hợp cho các hoạt động thể thao.");
        giaySneaker.setStatus(true);
        giaySneaker = productRepository.save(giaySneaker);
        
        // Giày tây
        Product giayTay = new Product();
        giayTay.setProductName("Giày tây nam công sở");
        giayTay.setCategory(giay);
        giayTay.setBrand("Aristino Business");
        giayTay.setSize("41");
        giayTay.setColor("Đen");
        giayTay.setMaterial("Da thật");
        giayTay.setGender("Nam");
        giayTay.setPrice(new BigDecimal("2500000"));
        giayTay.setStockQuantity(15);
        giayTay.setDescription("Giày tây nam công sở với chất liệu da thật cao cấp, thiết kế thanh lịch phù hợp cho công sở.");
        giayTay.setStatus(true);
        giayTay = productRepository.save(giayTay);
        
        // Thắt lưng
        Product thatLung = new Product();
        thatLung.setProductName("Thắt lưng da nam cao cấp");
        thatLung.setCategory(phuKien);
        thatLung.setBrand("Aristino");
        thatLung.setSize("L");
        thatLung.setColor("Nâu");
        thatLung.setMaterial("Da thật");
        thatLung.setGender("Nam");
        thatLung.setPrice(new BigDecimal("450000"));
        thatLung.setStockQuantity(35);
        thatLung.setDescription("Thắt lưng da nam cao cấp với chất liệu da thật, thiết kế tinh tế phù hợp cho mọi dịp.");
        thatLung.setStatus(true);
        thatLung = productRepository.save(thatLung);
        
        // Ví da
        Product viDa = new Product();
        viDa.setProductName("Ví da nam công sở");
        viDa.setCategory(phuKien);
        viDa.setBrand("Aristino Business");
        viDa.setSize("M");
        viDa.setColor("Đen");
        viDa.setMaterial("Da thật");
        viDa.setGender("Nam");
        viDa.setPrice(new BigDecimal("800000"));
        viDa.setStockQuantity(20);
        viDa.setDescription("Ví da nam công sở với thiết kế sang trọng, nhiều ngăn tiện lợi cho công việc.");
        viDa.setStatus(true);
        viDa = productRepository.save(viDa);
    }
}
