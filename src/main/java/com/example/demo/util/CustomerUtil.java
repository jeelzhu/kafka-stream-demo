package com.example.demo.util;

import com.example.demo.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Period;

public class CustomerUtil {
    private static final Logger logger = LoggerFactory.getLogger(CustomerUtil.class);

    public static boolean isValidCustomer(Customer customer) {
        if (customer == null) {
            logger.error("Received null customer");
            return false;
        }
        if (customer.firstName() == null || customer.firstName().trim().isEmpty()) {
            logger.error("Invalid customer first name: {}", customer);
            return false;
        }
        if (customer.lastName() == null || customer.lastName().trim().isEmpty()) {
            logger.error("Invalid customer last name: {}", customer);
            return false;
        }
        if (customer.dateOfBirth() == null || customer.dateOfBirth().isAfter(LocalDate.now())) {
            logger.error("Invalid customer DOB: {}", customer);
            return false;
        }
        return true;
    }

    public static boolean isEvenAge(Customer customer) {
        try {
            int age = Period.between(customer.dateOfBirth(), LocalDate.now()).getYears();
            return age % 2 == 0;
        } catch (Exception e) {
            logger.error("Error calculating age for customer: " + customer + ", Error: " + e.getMessage());
            return false;
        }
    }
}
