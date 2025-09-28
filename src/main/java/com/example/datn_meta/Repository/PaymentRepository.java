package com.example.datn_meta.Repository;

import com.example.datn_meta.Entity.PayMent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PayMent,Integer> {
}
