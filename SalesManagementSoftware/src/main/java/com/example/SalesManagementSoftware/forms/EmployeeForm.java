package com.example.SalesManagementSoftware.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeForm {

    @NotBlank(message = "Name is required")
    @Size(min=3, message = "Name must be at least 3 characters long")
    private String name;
    
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min=6, message = "Password must be at least 6 characters long")
    private String password;
    
    @NotBlank(message = "About is required")
    private String about;
    
    @Size(min=8, max=12, message="Invalid Phone Number")
    private String phoneNumber;
}


