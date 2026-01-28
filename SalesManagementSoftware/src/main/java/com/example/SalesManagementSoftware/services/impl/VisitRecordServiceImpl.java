package com.example.SalesManagementSoftware.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.SalesManagementSoftware.Helper.ResourceNotFoundException;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.VisitRecord;
import com.example.SalesManagementSoftware.repository.VisitRecordRepository;
import com.example.SalesManagementSoftware.services.VisitRecordService;

@Service
public class VisitRecordServiceImpl implements VisitRecordService {

    @Autowired
    private VisitRecordRepository repo;

    @Override
    public VisitRecord save(VisitRecord visitRecord) {

        return repo.save(visitRecord);
    }

    @Override
    public VisitRecord update(VisitRecord visitRecord) {
        VisitRecord existing = repo.findById(visitRecord.getId())
            .orElseThrow(() -> new RuntimeException("VisitRecord not found with id " + visitRecord.getId()));

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

        return repo.save(existing);
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
        repo.delete(record);
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
}

