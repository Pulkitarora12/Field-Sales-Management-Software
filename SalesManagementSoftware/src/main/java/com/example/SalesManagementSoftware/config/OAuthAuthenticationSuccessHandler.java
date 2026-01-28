package com.example.SalesManagementSoftware.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.Providers;
import com.example.SalesManagementSoftware.entity.Role;
import com.example.SalesManagementSoftware.repository.PageRepository;
import com.example.SalesManagementSoftware.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserService service;

    @Autowired
    private PageRepository repo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
//        logger.info("OAuthAuthenticationSuccessHandler");

        // Get user details from OAuth2
        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        // Identify the provider
        var oauth = (OAuth2AuthenticationToken) authentication;
        String clientId = oauth.getAuthorizedClientRegistrationId();
        
//        logger.info(clientId);

        String email = null;
        String name = null;
        String picture = null;

        if ("google".equals(clientId)) {
            email = user.getAttribute("email").toString();
            name = user.getAttribute("name").toString();
            picture = user.getAttribute("picture").toString();
        }

        // Check if user already exists
        Employee existingUser = repo.findByEmail(email).orElse(null);

        if (existingUser == null) {
            // Create new user
            Employee newUser = new Employee();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setProfileLink(picture);
            newUser.setPassword("password");
            newUser.setProvider(Providers.GOOGLE);
            newUser.setProviderURL(user.getName());
            newUser.setAbout("This account was created using google");
            newUser.setEnabled(false); // Initially disabled until email verification
            newUser.setEmailVerified(false);
            newUser.setRole(Role.EMPLOYEE);
            
            service.saveUser(newUser, true);
//            logger.info("New user created: " + email);
            
            // Redirect to verification page since user is not enabled
            response.sendRedirect("/login?error=verification");
            return;
        } else {
//            logger.info("Existing user logging in: " + email);
            
            // Check if existing user is enabled
            if (!existingUser.isEnabled()) {
//                logger.info("User not enabled, redirecting to verification: " + email);
                response.sendRedirect("/login?error=verification");
                return;
            }
            
            // User exists and is enabled - allow login
//            logger.info("User login successful: " + email);
        }

        // Redirect to profile if user is enabled
        response.sendRedirect("/user/profile");
    }
}