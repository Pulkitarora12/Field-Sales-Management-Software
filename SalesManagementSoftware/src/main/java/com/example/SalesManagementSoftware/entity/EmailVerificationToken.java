package com.example.SalesManagementSoftware.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {
    @Id
    private String token;

    private String userEmail;

    private LocalDateTime expiryDate;

    public EmailVerificationToken(String userEmail) {
        this.token = UUID.randomUUID().toString();
        this.userEmail = userEmail;
        this.expiryDate = LocalDateTime.now().plusHours(24); // 24 hrs validity
    }
}
