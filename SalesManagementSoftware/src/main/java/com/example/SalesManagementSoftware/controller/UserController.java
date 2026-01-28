package com.example.SalesManagementSoftware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SalesManagementSoftware.Helper.AppConstants;
import com.example.SalesManagementSoftware.Helper.Helper;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.Role;
import com.example.SalesManagementSoftware.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

     private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    //user profile page
    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {

        String email = Helper.getEmailOfLoggedInUser(authentication);
        Employee user = userService.getUserByEmail(email);

        if (user != null) {
            model.addAttribute("loggedInUser", user);
        } else {
            // Handle null case, e.g., redirect to login
            return "redirect:/login";
        }

        if (!user.isEnabled()) {
            return "login";
        }
        
        return "user/profile";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allEmployees")
    public String allEmployees(Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "employeeId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        String email = Helper.getEmailOfLoggedInUser(authentication);
        Employee user = userService.getUserByEmail(email);

        if (!user.isEnabled()) {
            return "login";
        }

        Page<Employee> pageEmp = userService.getAll(page, size, sortBy, direction);

        if (pageEmp == null) {
            pageEmp = Page.empty();
        }

        model.addAttribute("page", pageEmp);
        model.addAttribute("pageSize", size);
        
        return "user/allEmployees";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleteEmployee/{employeeId}")
    public String deleteEmployee(@PathVariable Long employeeId) {

        userService.deleteUser(employeeId);
        logger.info("Employee deleted: {}", employeeId);
        return "redirect:/user/allEmployees";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editRole/{employeeId}")
    public String editRole(@PathVariable Long employeeId) {

        Employee emp = userService.getUserById(employeeId).orElse(null);
        
        if (emp.getRole() == Role.ADMIN) {
            emp.setRole(Role.EMPLOYEE);
            System.out.println("Role changed to employee");
        } else {    
            emp.setRole(Role.ADMIN);
            System.out.println("Role changed to admin");
            System.out.println(emp.getRole());
        }

        userService.saveUser(emp, false);

        return "redirect:/user/allEmployees";
    }
}
