package com.example.SalesManagementSoftware.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.SalesManagementSoftware.entity.DailyRecord;
import com.example.SalesManagementSoftware.entity.Employee;

@Repository
public interface DailyVisitRepo extends JpaRepository<DailyRecord, Long> {

    // Count queries from VisitRecord table
    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE DATE(v.dateFilled) = :today AND v.employee = :employee")
    long countReportsSubmittedToday(@Param("today") LocalDate today, @Param("employee") Employee employee);

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.tally = true AND DATE(v.dateFilled) = :today AND v.employee = :employee")
    long countReportsWithTallyCheck(@Param("today") LocalDate today, @Param("employee") Employee employee);

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.agreedForDemo = true AND DATE(v.dateFilled) = :today AND v.employee = :employee")
    long countReportsAgreedForDemo(@Param("today") LocalDate today, @Param("employee") Employee employee);

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE v.opportunity = 'New License' AND DATE(v.dateFilled) = :today AND v.employee = :employee")
    long countReportsWithNewLicense(@Param("today") LocalDate today, @Param("employee") Employee employee);

    @Query("SELECT COUNT(v) FROM VisitRecord v WHERE ((v.opportunity = 'Other' AND v.otherOpportunities IS NOT NULL AND v.otherOpportunities != '') OR (v.otherOpportunities IS NOT NULL AND v.otherOpportunities != '' AND TRIM(v.otherOpportunities) != '')) AND DATE(v.dateFilled) = :today AND v.employee = :employee")
    long countReportsWithOtherOpportunities(@Param("today") LocalDate today, @Param("employee") Employee employee);

    // DailyRecord CRUD operations
    Page<DailyRecord> findByScoutName(Employee employee, Pageable pageable);

    Page<DailyRecord> findByScoutNameAndDateFilledBetween(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );

    Optional<DailyRecord> findByScoutNameAndDateFilled(Employee scoutName, LocalDate dateFilled);

    @Query("SELECT d FROM DailyRecord d WHERE d.scoutName = :employee AND d.dateFilled = :date")
    Page<DailyRecord> findByEmployeeAndDate(
            @Param("employee") Employee employee,
            @Param("date") LocalDate date,
            Pageable pageable
    );

    @Query("SELECT d FROM DailyRecord d WHERE d.dateFilled = :date")
    Page<DailyRecord> findAllByDate(
            @Param("date") LocalDate date,
            Pageable pageable
    );

}