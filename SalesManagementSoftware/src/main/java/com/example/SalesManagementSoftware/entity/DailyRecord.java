package com.example.SalesManagementSoftware.entity;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long noOfCompaniesVisited;

    private Long noOfCompaniesUsingTally;
   
    private Long noOfDemoAgreed;
   
    private Long otherOpportunities;

    private Long prospectForNewLicense;

    private LocalDate dateFilled;

    @ManyToOne
    private Employee scoutName;

}