package com.example.SalesManagementSoftware.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.VisitRecord;

public interface UserService {
    
    Employee saveUser(Employee user, boolean isNewUser);
    
    Optional<Employee> getUserById(Long id);
    
    Optional<Employee> updateUser(Employee user);
    
    void deleteUser(Long id);
    
    boolean isUserExists(Long userId);
    
    boolean isUserExistsByEmail(String email);
    
    List<Employee> getAllUsers();

    Employee getUserByEmail(String email);

    Employee getUserByEmailToken(String token);

    Page<Employee> getAll(int page, int size, String sortBy, String direction);

}
