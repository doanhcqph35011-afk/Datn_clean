package com.example.datn_meta.Repository;

import com.example.datn_meta.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserDao extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    Optional<Users> findByPhoneNumber(String phoneNumber);
    // OPTIONAL: tiện cho xác thực
    @Query("select u from Users u where u.email = :login or u.phoneNumber = :login")
    Optional<Users> findByEmailOrPhone(@Param("login") String login);
}
