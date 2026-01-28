package com.example.SalesManagementSoftware.Helper;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.example.SalesManagementSoftware.forms.VisitRecordForm;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("user/addVisitRecord");
        modelAndView.addObject("form", new VisitRecordForm());
        modelAndView.addObject("message", "An unexpected error occurred. Please try again.");
        System.out.println(ex);
        return modelAndView;
    }
}