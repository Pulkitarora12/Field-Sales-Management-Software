package com.example.SalesManagementSoftware.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scoutName;

    private String placeOfVisit;

    private String companyName;

    private String email;

    private String newOrRevisit;

    private String contactPersonName;

    private String contactNumber;

    private String natureOfBusiness;

    private String otherNatureOfBusiness;

    private Boolean computer;

    private Boolean tally;

    @Temporal(TemporalType.DATE)
    private Date lastUpgrade;

    private String serialNumber;

    private String opportunity; 
    // e.g. None/New license/Upgrade/TSS/Service/Customization

    private String otherOpportunities;

    private Boolean revisitRequired;

    private LocalDate dateOfRevisit;

    private Boolean agreedForDemo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFilled;

    @PrePersist
    protected void onCreate() {
        dateFilled = new Date();
    }

    @ManyToOne
    private Employee employee;

    @ElementCollection
    @CollectionTable(name = "visit_record_custom_fields", joinColumns = @JoinColumn(name = "visit_record_id"))
    @MapKeyColumn(name = "field_label")
    @Column(name = "field_value")
    private Map<String, String> customFields = new HashMap<>();

    private String otherSoftwares;
}
