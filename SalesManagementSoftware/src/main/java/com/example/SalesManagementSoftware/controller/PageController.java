package com.example.SalesManagementSoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.Role;
import com.example.SalesManagementSoftware.forms.EmployeeForm;
import com.example.SalesManagementSoftware.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String redirectHome() {
        return "login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {

        String message = (String) session.getAttribute("message");

        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }

        return "login"; // This will render login.html from templates
    }

    @GetMapping("/register")
    public String register(Model model, HttpSession session) {
        EmployeeForm employeeForm = new EmployeeForm();
        model.addAttribute("employeeForm", employeeForm);
        
        String message = (String) session.getAttribute("message");
//        System.out.println("Session attribute 'message' retrieved: " + message);

        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        
        return "register"; // This will render register.html from templates
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid EmployeeForm userForm,BindingResult result,HttpSession session) {
        
        //fetch data from form
       System.out.println(userForm);

        //validate form data
        if (result.hasErrors()) {
            return "register"; // no need to redirect just return to same page
        }

        //save to database
        // User user = User  we will not use builder as it will not save default values
        //             .builder()
        //             .name(userForm.getName())
        //             .email(userForm.getEmail())
        //             .password(userForm.getPassword())
        //             .about(userForm.getAbout())
        //             .phoneNumber(userForm.getPhoneNumber())
        //             .profileLink(defaultProfilePic)
        //             .build();
        
        //creating user object and saving manually
        Employee user = new Employee();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordEncoder.encode(userForm.getPassword()));
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setRole(Role.EMPLOYEE);

        Employee savedUser = userService.saveUser(user, true);

        //message that info is saved
        session.setAttribute("message", "User registered successfully!");
       System.out.println("Session attribute 'message' set: " + session.getAttribute("message"));


        //redirect to login page
        return "redirect:/register"; 
    }

    @ModelAttribute("loggedInUser")
    public Employee getLoggedInUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && 
            authentication.getPrincipal() instanceof Employee) {
            Employee loggedInUser = (Employee) authentication.getPrincipal();

            if (!loggedInUser.isEnabled()) {
                return null;
            }

            return loggedInUser;
        }
        return null;
    }
}
