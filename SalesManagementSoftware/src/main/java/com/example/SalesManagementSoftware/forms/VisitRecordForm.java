package com.example.SalesManagementSoftware.forms;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VisitRecordForm {

    @NotBlank(message = "Scout Name is required")
    @Size(min = 3, message = "Scout Name must be at least 3 characters long")
    private String scoutName;

    @NotBlank(message = "Place of Visit is required")
    private String placeOfVisit;

    @NotBlank(message = "Company Name is required")
    private String companyName;

    private String email;

    @NotBlank(message = "Please select New or Revisit")
    private String newOrRevisit;

    @NotBlank(message = "Contact Person Name is required")
    private String contactPersonName;

    @Pattern(
            regexp = "^[0-9\\-\\s()]{10,20}$",
            message = "Telephone number must be valid (digits, spaces, hyphens, brackets allowed)"
    )
    private String contactNumber;


    @NotBlank(message = "Nature of Business is required")
    private String natureOfBusiness;

    private String otherNatureOfBusiness;

    // You can keep these as booleans (checkboxes in the form)
    private boolean computer;
    private boolean tally;

    // Optional field
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastUpgrade;
    // Note: You'll need to parse this string to Date in the controller

    private String serialNumber;

    private String opportunity; 
    // e.g. None/New license/Upgrade/TSS/Service/Customization

    private String otherOpportunities;

    private LocalDate dateOfRevisit;

    private boolean revisitRequired;
    private boolean agreedForDemo;
    public void setId(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }

    private String otherSoftwares;
    
}