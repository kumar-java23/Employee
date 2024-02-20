package com.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bean.EmployeeRequest;
import com.bean.TaxResponse;

 @RestController
 public class TaxController {

    @PostMapping("/tax_deduction")
    public ResponseEntity<TaxResponse> calculateTax(@RequestBody EmployeeRequest request) {
        double yearlySalary = calculateYearlySalary(request.getMonthlySalary(), request.getDateOfJoining());
        double tax = calculateTaxAmount(yearlySalary);
        double cess = calculateCessAmount(yearlySalary);
        TaxResponse response = new TaxResponse(request.getEmployeeCode(), request.getFirstName(), request.getLastName(),
                yearlySalary, tax, cess);
        return ResponseEntity.ok(response);
    }

    private double calculateYearlySalary(double monthlySalary, LocalDate doj) {
        int monthsWorked = (int) ((LocalDate.now().toEpochDay() - doj.toEpochDay()) / 30);
        return monthlySalary * monthsWorked;
    }

    private double calculateTaxAmount(double yearlySalary) {
        double tax = 0;
        if (yearlySalary <= 250000) {
            tax = 0;
        } else if (yearlySalary <= 500000) {
            tax = (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary <= 1000000) {
            tax = 12500 + (yearlySalary - 500000) * 0.1;
        } else {
            tax = 62500 + (yearlySalary - 1000000) * 0.2;
        }
        return tax;
    }

    private double calculateCessAmount(double yearlySalary) {
        double cess = 0;
        if (yearlySalary > 2500000) {
            cess = (yearlySalary - 2500000) * 0.02;
        }
        return cess;
    }
}