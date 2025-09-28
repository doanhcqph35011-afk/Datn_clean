package com.example.datn_meta.Controller;

import com.example.datn_meta.Entity.PayMent;
import com.example.datn_meta.Repository.OrderRepository;
import com.example.datn_meta.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Payment")
public class PaymentController {
    private final PaymentRepository paymentRepository;
    private  final OrderRepository orderRepository;
    // danh sách thanh toán
//    @GetMapping("/list")
//    public String ListPayment(){
//        List<PayMent> payMents = new ArrayList<>();
//
//    }

}
