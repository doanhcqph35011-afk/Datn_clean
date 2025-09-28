package com.example.Datn_clean.Entity;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private String gender;
    private Date dateOfBirth;
    private String address;
}
