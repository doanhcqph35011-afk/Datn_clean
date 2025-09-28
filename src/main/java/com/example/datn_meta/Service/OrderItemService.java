package com.example.Datn_clean.Service;

import com.example.Datn_clean.Entity.OrderItem;
import com.example.Datn_clean.Entity.Product;
import com.example.Datn_clean.Repository.OrderItemRepository;
import com.example.Datn_clean.Repository.ProductRepository;
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









