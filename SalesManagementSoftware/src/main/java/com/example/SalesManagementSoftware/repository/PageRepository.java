package com.example.SalesManagementSoftware.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SalesManagementSoftware.entity.Employee;

@Repository
public interface PageRepository extends JpaRepository<Employee, Long> {
    // Define any custom query methods if needed

    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findByEmailAndPassword(String email, String password);

    Optional<Employee> findByEmailToken(String token);
}

