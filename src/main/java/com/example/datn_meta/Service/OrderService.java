package com.example.datn_meta.Service;

import com.example.datn_meta.Entity.order;
import com.example.datn_meta.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderService {
        private final OrderRepository orderRepository;

    public List<order> findAll() {
        return orderRepository.findAll();
    }


public order save(order order1) {
    if (order1.getOrder_id() != null) {
        // Đơn hàng đã tồn tại → update
        order existing = orderRepository.findById(order1.getOrder_id())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // cập nhật các field cơ bản
        existing.setCustomer(order1.getCustomer());
        existing.setOrder_date(order1.getOrder_date());
        existing.setStatus(order1.getStatus());
        existing.setShipping_address(order1.getShipping_address());
        existing.setTotal_amount(order1.getTotal_amount());
        existing.setPayment_method(order1.getPayment_method());

        // cập nhật orderItems an toàn
        existing.getOrderItems().clear();
        if (order1.getOrderItems() != null) {
            existing.getOrderItems().addAll(order1.getOrderItems());
        }

        return orderRepository.save(existing);
    } else {
        // đơn hàng mới → tạo mới
        return orderRepository.save(order1);
    }
}
    public order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteById(Integer id) {
        orderRepository.deleteById(id);
    }
}
