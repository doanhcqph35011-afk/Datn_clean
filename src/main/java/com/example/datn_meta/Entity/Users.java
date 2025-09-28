package com.example.Datn_clean.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "phone_Number")
        })
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name",nullable = false, length = 150)
    private String fullName;

    @Column(name="email", length = 150)
    private String email;   // Có thể null nếu đăng ký bằng sdt

    @Column(name = "phone_number", length = 10)
    private String phoneNumber; // Có thể null nếu đăng ký bằng email

    @Column(name="password",nullable = false, length = 255)
    private String password;  // Bắt buộc có (local thì hash, google/facebook dùng dummy)
    @Column(name = "gender")
    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "address")
    private String address;

    // Role dùng Enum
    @Enumerated(EnumType.STRING)
    @Column(name = "role",nullable = false, length = 50)
    private Role role = Role.USER;

    @Column(name = "provider",nullable = false, length = 50)
    private String provider = "local"; // local | google | facebook

    @Column(name = "provider_id", length = 200)
    private String providerId;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Enum cho vai trò
    public enum Role {
        USER,
        STAFF,
        ADMIN
    }
}
