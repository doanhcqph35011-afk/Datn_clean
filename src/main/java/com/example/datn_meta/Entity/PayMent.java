package com.example.Datn_clean.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "PayMent")
public class PayMent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payment_id;
    @Column(name = "order_id")
    private Integer order_id;
    @Column(name = "payment_method")
    private String  payment_method;
    @Column(name = "payment_status")
    private String payment_status;
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime payment_date = LocalDateTime.now();

}
