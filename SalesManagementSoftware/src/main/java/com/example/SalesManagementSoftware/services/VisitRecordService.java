package com.example.SalesManagementSoftware.services;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.VisitRecord;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.domain.Page;

public interface VisitRecordService {

    VisitRecord save(VisitRecord visitRecord);

    VisitRecord update(VisitRecord visitRecord);

    List<VisitRecord> getAll();

    VisitRecord getById(Long id);

    void delete(Long id);

    List<VisitRecord> getByUserId(Long userId);

    Page<VisitRecord> searchByScoutName(String scoutName, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByPlaceOfVisit(String placeOfVisit, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByCompanyName(String companyName, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByNewOrRevisit(String newOrRevisit, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByContactPersonName(String contactPersonName, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByContactPersonNumber(String contactPersonNumber, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByNatureOfBusiness(String natureOfBusiness, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByComputer(boolean computer, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByTally(boolean tally, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByRevisitRequired(boolean revisitRequired, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByAgreedForDemo(boolean agreedForDemo, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByLastUpgrade(String lastUpgrade, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> searchByOpportunity(String opportunity, int size, int page, String sortBy, String order, Employee employee);

    Page<VisitRecord> getByEmployee(Employee user, int page, int size, String sortBy, String direction);

    Page<VisitRecord> getAll(int page, int size, String sortBy, String direction);
}