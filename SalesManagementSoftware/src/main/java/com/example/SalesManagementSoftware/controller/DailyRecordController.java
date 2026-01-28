package com.example.SalesManagementSoftware.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SalesManagementSoftware.Helper.AppConstants;
import com.example.SalesManagementSoftware.Helper.Helper;
import com.example.SalesManagementSoftware.entity.DailyRecord;
import com.example.SalesManagementSoftware.entity.Employee;
import com.example.SalesManagementSoftware.services.DailyVisitService;
import com.example.SalesManagementSoftware.services.UserService;

@Controller
@RequestMapping("/user/visit")
public class DailyRecordController {

    @Autowired
    private DailyVisitService service;

    @Autowired
    private UserService userService;

    @GetMapping("/dailyReports")
    public String getEmployeeHistory(
            Model model,
            Authentication authentication,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE) int size,
            @RequestParam(defaultValue = "dateFilled") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        String email = Helper.getEmailOfLoggedInUser(authentication);
        Employee user = userService.getUserByEmail(email);

        if (!user.isEnabled()) {
            return "login";
        }

        // Get today's date and record
        LocalDate today = LocalDate.now();
        Optional<DailyRecord> todayRecordOpt = service.getDailyRecord(today, user);

        model.addAttribute("todayDate", today);
        model.addAttribute("todayRecord", todayRecordOpt.orElse(null));

        Page<DailyRecord> pageRecord;

        // Check if date filter is applied
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            pageRecord = service.getEmployeeHistoryBetweenDates(user, start, end, sortBy, direction, page, size);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } else {
            // Get complete history without filter
            pageRecord = service.getEmployeeFullHistory(user, sortBy, direction, page, size);
        }

        model.addAttribute("pageRecord", pageRecord);
        model.addAttribute("employee", user);

        return "user/dailyReports";
    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/allDailyReports")
//    public String getAllReportsCount(
//            Model model,
//            Authentication authentication,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE) int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "asc") String direction) {
//
//        // Always take today's date
//        LocalDate date = LocalDate.now();
//
//        String email = Helper.getEmailOfLoggedInUser(authentication);
//        Employee user = userService.getUserByEmail(email);
//
//        if (!user.isEnabled()) {
//            return "login";
//        }
//
//        Page<DailyRecord> pageRecord = service.getAllEmployeeDailyRecord(date, sortBy, direction, page, size);
//
//        model.addAttribute("pageRecord", pageRecord);
//
//        return "user/AllDailyReports";
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allEmployees")
    public String allEmployees(Model model,
                               Authentication authentication,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "" + AppConstants.PAGE_SIZE) int size,
                               @RequestParam(defaultValue = "employeeId") String sortBy,
                               @RequestParam(defaultValue = "asc") String direction) {

        String email = Helper.getEmailOfLoggedInUser(authentication);
        Employee user = userService.getUserByEmail(email);

        if (!user.isEnabled()) {
            return "login";
        }

        Page<Employee> pageEmp = userService.getAll(page, size, sortBy, direction);

        if (pageEmp == null) {
            pageEmp = Page.empty();
        }

        model.addAttribute("page", pageEmp);
        model.addAttribute("pageSize", size);

        return "user/allEmployees";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allDailyReports")
    public String getSelectedEmployeeHistory(
            @RequestParam("empId") Long empId,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateFilled") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {

        Employee employee = userService.getUserById(empId).orElse(null);

        if (employee == null) {
            model.addAttribute("error", "Employee not found!");
            return "user/allEmployees";
        }

        LocalDate today = LocalDate.now();
        var todayRecord = service.getDailyRecord(today, employee);

        model.addAttribute("todayDate", today);
        model.addAttribute("todayRecord", todayRecord.orElse(null));
        model.addAttribute("employee", employee);

        Page<DailyRecord> pageRecord;

        // Filter
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            LocalDate sDate = LocalDate.parse(startDate);
            LocalDate eDate = LocalDate.parse(endDate);

            pageRecord = service.getEmployeeHistoryBetweenDates(
                    employee, sDate, eDate, sortBy, direction, page, size);

            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);

        } else {
            pageRecord = service.getEmployeeFullHistory(employee, sortBy, direction, page, size);
        }

        model.addAttribute("pageRecord", pageRecord);

        return "user/dailyReports";
    }
}
