package com.example.datn_meta.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderDetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_detail_id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    @Column(name = "price_at_order")
    private Double price;

    // Không lưu total_price mà tính ra luôn
    @Transient
    public Double getTotalPrice() {
        return price * quantity;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer order_detail_id;
//
//    // Quan hệ nhiều OrderItem thuộc về 1 Order
//    @ManyToOne
//    @JoinColumn(name = "order_id", nullable = false)
//    private order order;
//
//    // Quan hệ nhiều OrderItem thuộc về 1 Product
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    private int quantity;
//
//  private BigDecimal price_at_order; // giá tại thời điểm mua
}
