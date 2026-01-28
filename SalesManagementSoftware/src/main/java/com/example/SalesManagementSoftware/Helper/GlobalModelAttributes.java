package com.example.SalesManagementSoftware.Helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.SalesManagementSoftware.repository.PageRepository;
import com.example.SalesManagementSoftware.entity.Employee;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private PageRepository repo;

    @ModelAttribute("loggedInUser")
    public Employee addLoggedInUserToModel(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return repo.findByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }
}

