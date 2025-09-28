package com.example.Datn_clean.Entity;
import lombok.Data;

@Data
public class LoginRequest {
    private String username; // email hoặc số điện thoại
    private String password;
}
