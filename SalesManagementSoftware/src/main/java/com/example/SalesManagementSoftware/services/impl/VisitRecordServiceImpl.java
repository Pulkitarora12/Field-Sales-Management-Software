package com.example.SalesManagementSoftware.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.Instant;
import com.example.SalesManagementSoftware.entity.DailyRecord;
import com.example.SalesManagementSoftware.services.DailyVisitService;
import com.example.SalesManagementSoftware.Helper.ResourceNotFoundException;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.VisitRecord;
import com.example.SalesManagementSoftware.repository.VisitRecordRepository;
import com.example.SalesManagementSoftware.services.VisitRecordService;

@Service
public class VisitRecordServiceImpl implements VisitRecordService {

    @Autowired
    private VisitRecordRepository repo;

    @Autowired
    private DailyVisitService dailyVisitService;

    private void updateDailyRecord(Employee employee, LocalDate date) {
        if (employee == null || date == null) return;
        
        long totalVisits = dailyVisitService.getTotalVisits(date, employee);
        
        if (totalVisits == 0) {
            dailyVisitService.getDailyRecord(date, employee).ifPresent(record -> {
                dailyVisitService.delete(record);
            });
            return;
        }
        
        long tallyChecked = dailyVisitService.getTallyChecked(date, employee);
        long agreedForDemo = dailyVisitService.getAgreedForDemo(date, employee);
        long newLicense = dailyVisitService.getNewLicense(date, employee);
        long otherOpportunities = dailyVisitService.getOtherOpportunties(date, employee);
        
        DailyRecord record = dailyVisitService.getDailyRecord(date, employee)
                .orElse(new DailyRecord());
        
        record.setScoutName(employee);
        record.setDateFilled(date);
        record.setNoOfCompaniesVisited(totalVisits);
        record.setNoOfCompaniesUsingTally(tallyChecked);
        record.setNoOfDemoAgreed(agreedForDemo);
        record.setProspectForNewLicense(newLicense);
        record.setOtherOpportunities(otherOpportunities);
        
        dailyVisitService.save(record);
    }

    @Override
    public VisitRecord save(VisitRecord visitRecord) {
        VisitRecord saved = repo.saveAndFlush(visitRecord);
        LocalDate date = saved.getDateFilled() != null 
            ? Instant.ofEpochMilli(saved.getDateFilled().getTime()).atZone(ZoneId.systemDefault()).toLocalDate()
            : LocalDate.now();
        updateDailyRecord(saved.getEmployee(), date);
        return saved;
    }

    @Override
    public VisitRecord update(VisitRecord visitRecord) {
        VisitRecord existing = repo.findById(visitRecord.getId())
            .orElseThrow(() -> new RuntimeException("VisitRecord not found with id " + visitRecord.getId()));

        Employee oldEmployee = existing.getEmployee();
        LocalDate oldDate = existing.getDateFilled() != null 
            ? Instant.ofEpochMilli(existing.getDateFilled().getTime()).atZone(ZoneId.systemDefault()).toLocalDate()
            : LocalDate.now();

        // copy updated fields
        existing.setCompanyName(visitRecord.getCompanyName());
        existing.setScoutName(visitRecord.getScoutName());
        existing.setPlaceOfVisit(visitRecord.getPlaceOfVisit());
        existing.setNewOrRevisit(visitRecord.getNewOrRevisit());
        existing.setContactPersonName(visitRecord.getContactPersonName());
        existing.setContactNumber(visitRecord.getContactNumber());
        existing.setNatureOfBusiness(visitRecord.getNatureOfBusiness());
        existing.setComputer(visitRecord.getComputer());
        existing.setTally(visitRecord.getTally());
        existing.setLastUpgrade(visitRecord.getLastUpgrade());
        existing.setOpportunity(visitRecord.getOpportunity());
        existing.setRevisitRequired(visitRecord.getRevisitRequired());
        existing.setAgreedForDemo(visitRecord.getAgreedForDemo());
        existing.setEmployee(visitRecord.getEmployee());
        existing.setOtherOpportunities(visitRecord.getOtherOpportunities());

        VisitRecord updated = repo.saveAndFlush(existing);
        
        LocalDate newDate = updated.getDateFilled() != null 
            ? Instant.ofEpochMilli(updated.getDateFilled().getTime()).atZone(ZoneId.systemDefault()).toLocalDate()
            : LocalDate.now();
            
        updateDailyRecord(updated.getEmployee(), newDate);
        
        if (!oldDate.equals(newDate) || (oldEmployee != null && !oldEmployee.equals(updated.getEmployee()))) {
            updateDailyRecord(oldEmployee, oldDate);
        }

        return updated;
    }

    @Override
    public List<VisitRecord> getAll() {
        return new ArrayList<>();
    }

    @Override
    public VisitRecord getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Record not found"));
    }

    @Override
    public void delete(Long id) {
        VisitRecord record = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Employee employee = record.getEmployee();
        LocalDate date = record.getDateFilled() != null 
            ? Instant.ofEpochMilli(record.getDateFilled().getTime()).atZone(ZoneId.systemDefault()).toLocalDate()
            : LocalDate.now();
            
        repo.delete(record);
        repo.flush();
        
        updateDailyRecord(employee, date);
    }

    @Override
    public List<VisitRecord> getByUserId(Long userId) {
        return repo.findByEmployeeId(userId);
    }

    public Page<VisitRecord> searchByScoutName(String scoutName, int size, int page, String sortBy, String order, Employee employee) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort); 

        return repo.findByEmployeeAndScoutNameContaining(employee, scoutName, pageable);
    }

    @Override
    public Page<VisitRecord> searchByPlaceOfVisit(String placeOfVisit, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndPlaceOfVisitContaining(employee, placeOfVisit, pageable);
    }

    @Override
    public Page<VisitRecord> searchByCompanyName(String companyName, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndCompanyNameContaining(employee, companyName, pageable);
    }

    @Override
    public Page<VisitRecord> searchByNewOrRevisit(String newOrRevisit, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndNewOrRevisitContaining(employee, newOrRevisit, pageable);
    }

    @Override
    public Page<VisitRecord> searchByContactPersonName(String contactPersonName, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndContactPersonNameContaining(employee, contactPersonName, pageable);
    }

    @Override
    public Page<VisitRecord> searchByContactPersonNumber(String contactPersonNumber, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndContactNumberContaining(employee, contactPersonNumber, pageable);
    }

    @Override
    public Page<VisitRecord> searchByNatureOfBusiness(String natureOfBusiness, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndNatureOfBusinessContaining(employee, natureOfBusiness, pageable);
    }

    @Override
    public Page<VisitRecord> searchByComputer(boolean computer, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndComputer(employee, computer, pageable);
    }

    @Override
    public Page<VisitRecord> searchByTally(boolean tally, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndTally(employee, tally, pageable);
    }

    @Override
    public Page<VisitRecord> searchByRevisitRequired(boolean revisitRequired, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndRevisitRequired(employee, revisitRequired, pageable);
    }

    @Override
    public Page<VisitRecord> searchByAgreedForDemo(boolean agreedForDemo, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndAgreedForDemo(employee, agreedForDemo, pageable);
    }

    @Override
    public Page<VisitRecord> searchByLastUpgrade(String lastUpgrade, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndLastUpgrade(employee, lastUpgrade, pageable);
    }

    @Override
    public Page<VisitRecord> searchByOpportunity(String opportunity, int size, int page, String sortBy, String order, Employee employee) {
        Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return repo.findByEmployeeAndOpportunityContaining(employee, opportunity, pageable);
    }

    @Override
    public Page<VisitRecord> getByEmployee(Employee user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return repo.findByEmployee(user, pageable); 
    }

    @Override
    public Page<VisitRecord> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                  : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable);
    }

    @Override
    public void syncDailyRecords() {
        List<VisitRecord> allRecords = repo.findAll();
        for (VisitRecord vr : allRecords) {
            if (vr.getEmployee() != null && vr.getDateFilled() != null) {
                LocalDate date = Instant.ofEpochMilli(vr.getDateFilled().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                updateDailyRecord(vr.getEmployee(), date);
            }
        }
    }
}

