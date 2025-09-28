package com.example.datn_meta.Repository;


import com.example.datn_meta.Entity.order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<order,Integer> {
}
