package com.example.Datn_clean.Repository;

import com.example.Datn_clean.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository  extends JpaRepository<Cust> {

}
