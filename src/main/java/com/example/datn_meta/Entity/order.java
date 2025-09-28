package com.example.Datn_clean.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;  // Liên kết tới bảng khách hàng

    private LocalDateTime order_date = LocalDateTime.now();

    private String status = "pending";  // pending, confirmed, shipping, completed, cancelled
    private String shipping_address;
    private Double total_amount;
    private String payment_method;
    @Transient
    private String payment_status = "unpaid"; // unpaid, paid, refund
    // 🔗 Liên kết tới chi tiết đơn hàng
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;
        // liên kết tới bản thanh toán

//    // 🔗 Liên kết tới thông tin giao hàng
//    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
//    private Shipping shipping;
}
