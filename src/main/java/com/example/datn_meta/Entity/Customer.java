package com.example.datn_meta.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Customers")
@Entity
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Customer_id;
    private String full_name;
    @Column(unique = true)
    private String email;
    private String phone_number;
    private String address;
    private String password;
    private String gender;
    private LocalDate date_of_birth;
//    private String address;
    private Integer loyalty_points = 0;

    @Column(updatable = false)
    private java.time.LocalDateTime created_at = java.time.LocalDateTime.now();
    public List<order> getOrders() {
        return this.getOrders();
    }
}
