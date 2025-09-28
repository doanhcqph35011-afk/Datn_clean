package com.example.Datn_clean.Repository;

import com.example.Datn_clean.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
}
