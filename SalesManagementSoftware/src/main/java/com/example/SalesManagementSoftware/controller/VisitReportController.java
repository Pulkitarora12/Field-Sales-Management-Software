package com.example.SalesManagementSoftware.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SalesManagementSoftware.Helper.AppConstants;
import com.example.SalesManagementSoftware.Helper.Helper;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.entity.VisitRecord;
import com.example.SalesManagementSoftware.forms.VisitRecordForm;
import com.example.SalesManagementSoftware.services.UserService;
import com.example.SalesManagementSoftware.services.VisitRecordService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/visit")
public class VisitReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private VisitRecordService service;

    private static final Logger logger = LoggerFactory.getLogger(VisitReportController.class);

    @GetMapping("/add")
    public String addReport(Model model, HttpSession session, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        Employee user = userService.getUserByEmail(email);

        if (user != null) {
            model.addAttribute("loggedInUser", user);
        } else {
            // Handle null case, e.g., redirect to login
            return "redirect:/login";
        }

        if (!user.isEnabled()) {
            return "login";
        }
        
        VisitRecordForm form = new VisitRecordForm();
        form.setScoutName(user.getName());
        model.addAttribute("form", form);
        
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        return "user/addVisitRecord"; // Template expects 'loggedInUser' but it's not provided
    }

    @PostMapping("/add")
    public String saveReport(@ModelAttribute @Valid VisitRecordForm form,
                             BindingResult result,
                             Authentication authentication,
                             HttpSession session,
                             Model model,
                             @RequestParam(required = false) List<String> customLabels,
                             @RequestParam(required = false) List<String> customValues) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.toString()));
            model.addAttribute("form", form);
            model.addAttribute("message", "Please resolve the errors");
            return "user/addVisitRecord";
        }

        try {
            String email = Helper.getEmailOfLoggedInUser(authentication);
            Employee user = userService.getUserByEmail(email);

            if (user == null) {
                model.addAttribute("form", form);
                model.addAttribute("message", "User not found. Please log in again.");
                return "user/addVisitRecord";
            }

            if (!user.isEnabled()) {
                return "login";
            }

            VisitRecord record = new VisitRecord();
            record.setScoutName(form.getScoutName());
            record.setPlaceOfVisit(form.getPlaceOfVisit());
            record.setCompanyName(form.getCompanyName());
            record.setNewOrRevisit(form.getNewOrRevisit());
            record.setContactPersonName(form.getContactPersonName());
            record.setContactNumber(form.getContactNumber());
            record.setNatureOfBusiness(form.getNatureOfBusiness());
            record.setOtherNatureOfBusiness(form.getOtherNatureOfBusiness());
            record.setComputer(form.isComputer());
            record.setTally(form.isTally());
            record.setLastUpgrade(form.getLastUpgrade());
            record.setOpportunity(form.getOpportunity());
            record.setRevisitRequired(form.isRevisitRequired());
            record.setAgreedForDemo(form.isAgreedForDemo());
            record.setOtherOpportunities(form.getOtherOpportunities());
            record.setEmployee(user);
            record.setDateOfRevisit(form.getDateOfRevisit());
            record.setSerialNumber(form.getSerialNumber());
            record.setEmail(form.getEmail());
            record.setOtherSoftwares(form.getOtherSoftwares());

            if (customLabels != null && customValues != null && customLabels.size() == customValues.size()) {
                for (int i = 0; i < customLabels.size(); i++) {
                    record.getCustomFields().put(customLabels.get(i), customValues.get(i));
                }
            }

            service.save(record);
            session.setAttribute("message", "You have successfully added a new visit record");
            return "redirect:/user/visit/add"; // Redirect to avoid form resubmission
        } catch (Exception e) {
            logger.error("Error saving visit record", e);
            model.addAttribute("form", form);
            model.addAttribute("message", "An error occurred while saving the visit record. Please try again.");
            return "user/addVisitRecord";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public String viewReports(
            Model model,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        // 1. Get logged-in user
        String email = Helper.getEmailOfLoggedInUser(authentication);
        Employee user = userService.getUserByEmail(email);
        
        if (user != null) {
            model.addAttribute("loggedInUser", user);
        } else {
            // Handle null case, e.g., redirect to login
            return "redirect:/login";
        }

        if (!user.isEnabled()) {
            return "login";
        }

        // 2. Fetch paginated visit records for this user
        Page<VisitRecord> pageVisitRecord =
                service.getByEmployee(user, page, size, sortBy, direction);

        model.addAttribute("pageVisitRecord", pageVisitRecord);
        model.addAttribute("pageSize", size);

        return "user/reports"; // This should match your Thymeleaf template path
    }



    @GetMapping("/search")
    public String search(
            Model model,
            Authentication authentication,
            @RequestParam("field") String field,
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        // 1. Get logged-in user
        logger.info("field {} keyword {}", field, keyword);

        Page<VisitRecord> pageVisitRecord = null;

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        if (user != null) {
            model.addAttribute("loggedInUser", user);
        } else {
            // Handle null case, e.g., redirect to login
            return "redirect:/login";
        }

        if (field.equalsIgnoreCase("scoutName")) {
            pageVisitRecord = service.searchByScoutName(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("companyName")) {
            pageVisitRecord = service.searchByCompanyName(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("contactPersonName")) {
            pageVisitRecord = service.searchByContactPersonName(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("placeOfVisit")) {
            pageVisitRecord = service.searchByPlaceOfVisit(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("newOrRevisit")) {
            pageVisitRecord = service.searchByNewOrRevisit(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("contactNumber")) {
            pageVisitRecord = service.searchByContactPersonNumber(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("natureOfBusiness")) {
            pageVisitRecord = service.searchByNatureOfBusiness(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("computer")) {  
            boolean input = keyword.equals("true") ? true : false;
            pageVisitRecord = service.searchByComputer(input, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("tally")) {
            boolean input = keyword.equals("true") ? true : false;
            pageVisitRecord = service.searchByTally(input, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("opportunity")) {
            pageVisitRecord = service.searchByOpportunity(keyword, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("revisitRequired")) {
            boolean input = keyword.equals("true") ? true : false;
            pageVisitRecord = service.searchByRevisitRequired(input, size, page, sortBy, direction, user);
        } else if (field.equalsIgnoreCase("agreedForDemo")) {
            boolean input = keyword.equals("true") ? true : false;
            pageVisitRecord = service.searchByAgreedForDemo(input, size, page, sortBy, direction, user);
        }
        
        model.addAttribute("pageVisitRecord", pageVisitRecord);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE); 

        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);

        return "user/search";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteVisitRecord(@PathVariable Long id) {
        service.delete(id);
//        logger.info("Visit record deleted: {}", id);
        return "redirect:/user/visit";
    }

    @GetMapping("/view/{id}")
    public String updateView (@PathVariable Long id, Model model) {
//        logger.info("Viewing visit record: {}", id);

        var record = service.getById(id);

        VisitRecordForm form = new VisitRecordForm();
        
        form.setScoutName(record.getScoutName());
        form.setPlaceOfVisit(record.getPlaceOfVisit());
        form.setCompanyName(record.getCompanyName());
        form.setNewOrRevisit(record.getNewOrRevisit());
        form.setContactPersonName(record.getContactPersonName());
        form.setContactNumber(record.getContactNumber());
        form.setNatureOfBusiness(record.getNatureOfBusiness());
        form.setComputer(record.getComputer());
        form.setTally(record.getTally());
        form.setLastUpgrade(record.getLastUpgrade());
        form.setOpportunity(record.getOpportunity());
        form.setRevisitRequired(record.getRevisitRequired());
        form.setAgreedForDemo(record.getAgreedForDemo());
        form.setOtherOpportunities(record.getOtherOpportunities());
        form.setDateOfRevisit(record.getDateOfRevisit());

        model.addAttribute("form", form);
        model.addAttribute("id", id); 

        return "user/updateReport";
    }

    @GetMapping("/user/visit/update/{id}")
    public String updateVisitRecord (@ModelAttribute @Valid VisitRecordForm form,
                            @PathVariable Long id,
                            BindingResult result,
                            Authentication authentication,
                            HttpSession session,
                            Model model) {
        
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
//                logger.error("Validation error: {}", error.toString());
                if (error instanceof FieldError fieldError) {
                    logger.error("Field: {}, Message: {}", fieldError.getField(), fieldError.getDefaultMessage());
                }
            });
            model.addAttribute("form", form);
            model.addAttribute("message", "Please resolve the errors");
            return "user/updateVisitRecord";  // better than "addVisitRecord"
        }

        String email = Helper.getEmailOfLoggedInUser(authentication);
        Employee emp = userService.getUserByEmail(email);

        if (emp != null) {
            model.addAttribute("loggedInUser", emp);
        } else {
            // Handle null case, e.g., redirect to login
            return "redirect:/login";
        }

        if (!emp.isEnabled()) {
            return "login";
        }

        VisitRecord record = new VisitRecord();
        record.setId(id);  // important so JPA treats it as update
        record.setCompanyName(form.getCompanyName());
        record.setScoutName(form.getScoutName());
        record.setPlaceOfVisit(form.getPlaceOfVisit());
        record.setNewOrRevisit(form.getNewOrRevisit());
        record.setContactPersonName(form.getContactPersonName());
        record.setContactNumber(form.getContactNumber());
        record.setNatureOfBusiness(form.getNatureOfBusiness());
        record.setComputer(form.isComputer());
        record.setTally(form.isTally());
        record.setLastUpgrade(form.getLastUpgrade());
        record.setOpportunity(form.getOpportunity());
        record.setRevisitRequired(form.isRevisitRequired());
        record.setAgreedForDemo(form.isAgreedForDemo());
        record.setOtherOpportunities(form.getOtherOpportunities());
        record.setEmployee(emp);
        record.setDateOfRevisit(form.getDateOfRevisit());

        service.update(record);

        return "user/reports";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allReports")
    public String allReports(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        // 1. Fetch all visit records with pagination
        Page<VisitRecord> pageVisitRecord = service.getAll(page, size, sortBy, direction);

        // 2. Add to model
        model.addAttribute("pageVisitRecord", pageVisitRecord);
        model.addAttribute("pageSize", size);

        return "user/allReports"; // Thymeleaf template for all reports
    }

    // @GetMapping("/dailyReports")
    // public String dailyReports() {
    //     return "user/dailyReports";
    // }


}