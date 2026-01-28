package com.example.SalesManagementSoftware.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SalesManagementSoftware.services.impl.*;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send-test-email")
    public String sendTestEmail() {
        
        emailService.sendEmail("pulkitpulkitarr@gmail.com", "Test Email", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent vel lorem nec quam gravida posuere. Sed ac dolor sapien. Integer ullamcorper turpis a bibendum vulputate. Morbi finibus sapien at sapien malesuada, ac efficitur leo aliquam. Nullam vel ligula ac est volutpat malesuada. Mauris congue leo non libero tincidunt, sit amet porta elit laoreet. Aliquam eu facilisis neque, non rhoncus turpis. Sed eu eros justo. Integer malesuada erat vel gravida aliquet. Sed sagittis nisi ut posuere vehicula.");
        return "Email sent!";
    }
}