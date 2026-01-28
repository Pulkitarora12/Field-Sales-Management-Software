package com.example.SalesManagementSoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.SalesManagementSoftware.Helper.Helper;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.services.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void AddLoggedInUser(Model model, Authentication authentication) {

        if (authentication == null) {
            return;
        }
        
        //fetching email when logged in
        String username = Helper.getEmailOfLoggedInUser(authentication);
        
        //fetching data from db once fetched email

        Employee user = userService.getUserByEmail(username);

        if (user != null) {
            model.addAttribute("name", user.getName());
        } else {
            model.addAttribute("name", "Guest");
        }

        model.addAttribute("loggedInUser", user);
    }
}