package com.example.SalesManagementSoftware.services.impl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.SalesManagementSoftware.entity.DailyRecord;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.repository.DailyVisitRepo;
import com.example.SalesManagementSoftware.services.DailyVisitService;

@Service
public class DailyVisitServiceImpl implements DailyVisitService {

    @Autowired
    private DailyVisitRepo dailyVisitRepo;

    public void save(DailyRecord dailyRecord) {
        dailyVisitRepo.save(dailyRecord);
    }

    public void update(DailyRecord dailyRecord, LocalDate date) {
        Employee emp = dailyRecord.getScoutName();
        DailyRecord record = dailyVisitRepo.findByScoutNameAndDateFilled(emp, date).orElse(null);

        if (record == null) {
            dailyVisitRepo.save(dailyRecord);
        } else {
            record.setNoOfCompaniesUsingTally(dailyRecord.getNoOfCompaniesUsingTally());
            record.setNoOfCompaniesVisited(dailyRecord.getNoOfCompaniesVisited());
            record.setNoOfDemoAgreed(dailyRecord.getNoOfDemoAgreed());
            record.setOtherOpportunities(dailyRecord.getOtherOpportunities());
            record.setProspectForNewLicense(dailyRecord.getProspectForNewLicense());
            dailyVisitRepo.save(record);
        }
    }

    @Override
    public Long getTotalVisits(LocalDate today, Employee employee) {
        return dailyVisitRepo.countReportsSubmittedToday(today, employee);
    }

    @Override
    public Long getTallyChecked(LocalDate today, Employee employee) {
        return dailyVisitRepo.countReportsWithTallyCheck(today, employee);
    }

    @Override
    public Long getAgreedForDemo(LocalDate today, Employee employee) {
        return dailyVisitRepo.countReportsAgreedForDemo(today, employee);
    }

    @Override
    public Long getNewLicense(LocalDate today, Employee employee) {
        return dailyVisitRepo.countReportsWithNewLicense(today, employee);
    }

    @Override
    public Long getOtherOpportunties(LocalDate today, Employee employee) {
        return dailyVisitRepo.countReportsWithOtherOpportunities(today, employee);
    }

    @Override
    public Optional<DailyRecord> getDailyRecord(LocalDate date, Employee employee) {
        return dailyVisitRepo.findByScoutNameAndDateFilled(employee, date);
    }

    @Override
    public Page<DailyRecord> getEmployeeDailyRecord(LocalDate date, Employee employee, String sortBy, String order,
                                                    int page, int size) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return dailyVisitRepo.findByEmployeeAndDate(employee, date, pageable);
    }

    @Override
    public Page<DailyRecord> getAllEmployeeDailyRecord(LocalDate date, String sortBy, String order, int page,
                                                       int size) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return dailyVisitRepo.findAllByDate(date, pageable);
    }

    @Override
    public Page<DailyRecord> getEmployeeFullHistory(
            Employee employee,
            String sortBy,
            String order,
            int page,
            int size) {

        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);
        return dailyVisitRepo.findByScoutName(employee, pageable);
    }

    @Override
    public Page<DailyRecord> getEmployeeHistoryBetweenDates(
            Employee employee,
            LocalDate start,
            LocalDate end,
            String sortBy,
            String order,
            int page,
            int size) {

        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);
        return dailyVisitRepo.findByScoutNameAndDateFilledBetween(employee, start, end, pageable);
    }
}