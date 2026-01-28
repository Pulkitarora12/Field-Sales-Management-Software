package com.example.SalesManagementSoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class VerificationController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/verify-email")
    public String verifyEmail(
        @RequestParam String token,
        HttpSession session,
        Model model
    ) {

        System.out.println("Verification completed");

        Employee user = userService.getUserByEmailToken(token);

        if (user != null) {

            if (user.getEmailToken().equals(token)) {

                user.setEnabled(true);
                user.setEmailVerified(true);   // assuming you have a field in your entity
                user.setEmailToken(null);      // clear the token so it can't be reused
                userService.saveUser(user, false);

                System.out.println("Redirecting");
                session.setAttribute("message", "Please Log in.");
                return "redirect:/login";
            } else {

                System.out.println("Verification failed");
            }
        } else {
            System.out.println("email not found");
        } 

        

        session.setAttribute("message", "Invalid or Expired Link.");
        return "login";
    }
}

