package com.example.SalesManagementSoftware.Helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    private static String baseUrl;

    @Value("${server.baseUrl}")
    public void setBaseUrl(String url) {
        Helper.baseUrl = url;
    }
    
    public static String getEmailOfLoggedInUser(Authentication authentication) {
        
        Logger logger = LogManager.getLogger(Helper.class);

        
        if (authentication == null) {
            return null; // or throw a custom exception
        }

        String email = null;

        if (authentication instanceof OAuth2AuthenticationToken) {
            var token = (OAuth2AuthenticationToken) authentication;
            var registerId = token.getAuthorizedClientRegistrationId();
            var principal = (DefaultOAuth2User) authentication.getPrincipal();

            if ("google".equalsIgnoreCase(registerId)) {
                email = principal.getAttribute("email");
            } else {
                logger.warn("OAuth2 provider not handled: " + registerId);
            }

        } else {
            email = authentication.getName();
        }

        return email;
    }


    public static String getLinkForEmailVerification(String emailToken) {

        String link = "http://103.148.204.44:8080/auth/verify-email?token=" + emailToken;

        return link;
    }

}
