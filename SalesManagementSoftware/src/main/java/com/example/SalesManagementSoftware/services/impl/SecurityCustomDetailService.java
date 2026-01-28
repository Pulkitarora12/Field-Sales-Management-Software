package com.example.SalesManagementSoftware.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SalesManagementSoftware.entity.*;
import com.example.SalesManagementSoftware.repository.PageRepository;


@Service
public class SecurityCustomDetailService implements UserDetailsService {
    
    @Autowired
    private PageRepository pageRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> optionalUser = pageRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        Employee user = optionalUser.get();

        // Add this check to block login if email not verified
        // if (!user.isEnabled()) {
        //     throw new DisabledException("Please verify your email before logging in.");
        // }

        return user;
    }
}
