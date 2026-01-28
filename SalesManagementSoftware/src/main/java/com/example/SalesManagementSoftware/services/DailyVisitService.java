package com.example.SalesManagementSoftware.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.example.SalesManagementSoftware.entity.DailyRecord;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.repository.DailyVisitRepo;

public interface DailyVisitService {

    Long getTotalVisits(LocalDate today, Employee employee);

    Long getTallyChecked(LocalDate today, Employee employee);

    Long getAgreedForDemo(LocalDate today, Employee employee);

    Long getNewLicense(LocalDate today, Employee employee);
    
    Long getOtherOpportunties(LocalDate today, Employee employee);

    Optional<DailyRecord> getDailyRecord(LocalDate date, Employee employee);

    void update(DailyRecord record, LocalDate date);

    Page<DailyRecord> getEmployeeDailyRecord(LocalDate date, Employee employee, String sortBy, String order, int page, int size);

    Page<DailyRecord> getAllEmployeeDailyRecord(LocalDate date, String sortBy, String order, int page, int size);


    Page<DailyRecord> getEmployeeFullHistory(
            Employee employee,
            String sortBy,
            String order,
            int page,
            int size);

    Page<DailyRecord> getEmployeeHistoryBetweenDates(
            Employee employee,
            LocalDate start,
            LocalDate end,
            String sortBy,
            String order,
            int page,
            int size);
}
