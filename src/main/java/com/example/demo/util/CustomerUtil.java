package com.example.demo.util;

import com.example.demo.entity.Customer;

import java.time.LocalDate;
import java.time.Period;

public class CustomerUtil {
    public static boolean isValidCustomer(Customer customer) {
        if (customer == null) {
            System.err.println("Received null customer");
            return false;
        }

        if (customer.firstName() == null || customer.firstName().trim().isEmpty()) {
            System.err.println("Invalid customer first name: " + customer);
            return false;
        }

        if (customer.lastName() == null || customer.lastName().trim().isEmpty()) {
            System.err.println("Invalid customer last name: " + customer);
            return false;
        }

        if (customer.dateOfBirth() == null || customer.dateOfBirth().isAfter(LocalDate.now())) {
            System.err.println("Invalid customer DOB: " + customer);
            return false;
        }

        return true;
    }

    public static boolean isEvenAge(Customer customer) {
        try {
            int age = Period.between(customer.dateOfBirth(), LocalDate.now()).getYears();
            return age % 2 == 0;
        } catch (Exception e) {
            System.err.println("Error calculating age for customer: " + customer + ", Error: " + e.getMessage());
            return false;
        }
    }
}
