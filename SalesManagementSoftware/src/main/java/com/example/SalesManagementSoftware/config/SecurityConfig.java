package com.example.SalesManagementSoftware.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.SalesManagementSoftware.services.impl.SecurityCustomDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    
    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private SecurityCustomDetailService userDetailService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        //userdetailsService ka object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        //password encoder ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationFailureHandler customFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                                                HttpServletResponse response,
                                                AuthenticationException exception)
                                                throws IOException, ServletException {

                if (exception instanceof DisabledException) {
                    // User is disabled (email not verified)
                    response.sendRedirect("/login?error=verification");
                } else {
                    // Other login failures (e.g., bad credentials)
                    response.sendRedirect("/login?error=true");
                }
            }
        };
    }

    // @Bean
    // public AuthenticationSuccessHandler customSuccessHandler() {
    //     return new AuthenticationSuccessHandler() {
    //         @Override
    //         public void onAuthenticationSuccess(HttpServletRequest request, 
    //                                             HttpServletResponse response,
    //                                             org.springframework.security.core.Authentication authentication) 
    //                                             throws IOException, ServletException {
                
    //             // Get user role and redirect accordingly
    //             String role = authentication.getAuthorities().iterator().next().getAuthority();
                
    //             if (role.equals("ROLE_ADMIN")) {
    //                 response.sendRedirect("/admin/dashboard");
    //             } else if (role.equals("ROLE_MANAGER")) {
    //                 response.sendRedirect("/manager/dashboard");
    //             } else {
    //                 response.sendRedirect("/user/profile");
    //             }
    //         }
    //     };
    // }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authenticationProvider(authenticationProvider());

        
        httpSecurity.authorizeHttpRequests(auth -> {
            auth
                .requestMatchers("/user/allEmployees").hasRole("ADMIN")
                .requestMatchers("/user/visit").hasRole("ADMIN")
                .requestMatchers("/user/visit/allReports").hasRole("ADMIN")
                .requestMatchers("/user/visit/allDailyReports").hasRole("ADMIN")
                .requestMatchers("/user/editRole/**").hasRole("ADMIN")
                .requestMatchers("/user/deleteEmployee/**").hasRole("ADMIN")
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll();
        });


        
        httpSecurity.formLogin(formLogin -> {
            formLogin.loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .defaultSuccessUrl("/user/profile", true)
            .failureHandler(customFailureHandler())
            .usernameParameter("email")
            .passwordParameter("password"); 

        }); 

        //authentication failed exception
       
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        //oauth configuration
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login")
            .successHandler(handler);
        });
        
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
