package com.example.SalesManagementSoftware.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.VisitRecord;

@Repository
public interface VisitRecordRepository extends JpaRepository<VisitRecord, Long> {
    
    @Query("SELECT c FROM VisitRecord c WHERE c.employee.id = :userId")
    List<VisitRecord> findByEmployeeId(@Param("employeeId") Long userId);

    @EntityGraph(attributePaths = "employee")
    Page<VisitRecord> findByEmployee(Employee user, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndScoutNameContaining(Employee user, String scoutName, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndPlaceOfVisitContaining(Employee user, String placeOfVisit, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndCompanyNameContaining(Employee user, String companyName, Pageable pageable);

    @Query("SELECT v FROM VisitRecord v WHERE v.employee = ?1 AND v.newOrRevisit LIKE %?2%")
    Page<VisitRecord> findByEmployeeAndNewOrRevisitContaining(Employee user, String newOrRevisit, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndContactPersonNameContaining(Employee user, String contactPersonName, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndContactNumberContaining(Employee user, String contactPersonNumber, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndNatureOfBusinessContaining(Employee user, String natureOfBusiness, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndComputer(Employee employee, boolean computer, Pageable pageable);
    
    Page<VisitRecord> findByEmployeeAndTally(Employee employee, boolean tally, Pageable pageable);
    
    Page<VisitRecord> findByEmployeeAndRevisitRequired(Employee employee, boolean revisitRequired, Pageable pageable);
    
    Page<VisitRecord> findByEmployeeAndAgreedForDemo(Employee employee, boolean agreedForDemo, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndLastUpgrade(Employee user, String lastUpgrade, Pageable pageable);

    Page<VisitRecord> findByEmployeeAndOpportunityContaining(Employee user, String opportunity, Pageable pageable);

    List<VisitRecord> findByDateOfRevisit(LocalDate date);

}