package com.example.SalesManagementSoftware.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.VisitRecord;
import com.example.SalesManagementSoftware.repository.VisitRecordRepository;

@Service
public class ReminderScheduler {
    
    @Autowired
    private VisitRecordRepository reportRepository;

    @Autowired
    private EmailService emailService;

    // Runs every day at 10
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<VisitRecord> reports = reportRepository.findByDateOfRevisit(tomorrow);

        for (VisitRecord report : reports) {
            Employee emp = report.getEmployee();
            if (emp != null && emp.getEmail() != null) {
                String subject = "Reminder: Upcoming Visit Tomorrow";
                String body = "Dear " + emp.getName() +
                              ",\n\nThis is a reminder that you have a scheduled revisit on " +
                              report.getDateOfRevisit() + "for company " + report.getCompanyName() + ".\n\nRegards,\n Accounts Bureau";

                emailService.sendEmail(emp.getEmail(), subject, body);
            }
        }
    }
}
