package com.example.datn_meta.Service;

import com.example.datn_meta.Entity.OrderItem;
import com.example.datn_meta.Entity.Product;
import com.example.datn_meta.Repository.OrderItemRepository;
import com.example.datn_meta.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderItemService {
   private final OrderItemRepository orderItemRepository;
   private final ProductRepository productRepository;
    public List<OrderItem> fillAll(){
        return orderItemRepository.findAll();
    }
    public OrderItem findByid(Integer id){
        return orderItemRepository.findById(id).orElse(null);
    }
    public OrderItem save(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
    }
    public  void deleteById(Integer id){
        orderItemRepository.deleteById(id);
    }




}









