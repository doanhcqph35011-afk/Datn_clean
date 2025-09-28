package com.example.datn_meta.Service;

import com.example.datn_meta.Entity.PayMent;
import com.example.datn_meta.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    public List<PayMent>fillALL(){
        return paymentRepository.findAll();
    }
        public PayMent findById(Integer  id){
                return paymentRepository.findById(id).orElse(null);
        }
        public PayMent save(PayMent payMent){
        return paymentRepository.save(payMent);

    }
    public void deleteById(Integer id){
        paymentRepository
    }
}
