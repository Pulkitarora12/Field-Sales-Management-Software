package com.example.SalesManagementSoftware;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.Role;
import com.example.SalesManagementSoftware.repository.PageRepository;

@SpringBootApplication
@RestController
@EnableScheduling
public class SalesManagementSoftwareApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private PageRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(SalesManagementSoftwareApplication.class, args);
    }

    // This method is required for WAR deployment
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SalesManagementSoftwareApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        Employee emp = new Employee();
        emp.setRole(Role.ADMIN);
        emp.setEmail("admin@gmail.com");
        emp.setPassword(encoder.encode("admin"));
        emp.setName("Admin");
        emp.setEmailVerified(true);
        emp.setEnabled(true);
        emp.setAbout("This is a dummy user");
        emp.setPhoneNumber("7894561230");

        repo.findByEmail("admin@gmail.com").ifPresentOrElse(user1 -> {}, () -> {
            repo.save(emp);
            System.out.println("User created");
        });
    }
}
