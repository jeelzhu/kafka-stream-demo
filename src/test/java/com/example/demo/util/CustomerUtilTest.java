package com.example.demo.util;

import com.example.demo.entity.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerUtilTest {
    @Test
    public void testIsValidCustomer_ValidCustomer() {
        Customer customer = new Customer("Alice", "Smith", LocalDate.of(1990, 1, 1));
        boolean result = CustomerUtil.isValidCustomer(customer);
        assertTrue(result, "Expected customer to be valid");
    }

    @Test
    public void testIsValidCustomer_InvalidCustomer_NullCustomer() {
        Customer customer = null;
        boolean result = CustomerUtil.isValidCustomer(customer);
        assertFalse(result, "Expected null customer to be invalid");
    }

    @Test
    public void testIsValidCustomer_InvalidCustomer_EmptyFirstName() {
        Customer customer = new Customer("", "Smith", LocalDate.of(1990, 1, 1));
        boolean result = CustomerUtil.isValidCustomer(customer);
        assertFalse(result, "Expected customer with empty first name to be invalid");
    }

    @Test
    public void testIsValidCustomer_InvalidCustomer_EmptyLastName() {
        Customer customer = new Customer("Alice", "", LocalDate.of(1990, 1, 1));
        boolean result = CustomerUtil.isValidCustomer(customer);
        assertFalse(result, "Expected customer with empty last name to be invalid");
    }

    @Test
    public void testIsValidCustomer_InvalidCustomer_NullDateOfBirth() {
        Customer customer = new Customer("Alice", "Smith", null);
        boolean result = CustomerUtil.isValidCustomer(customer);
        assertFalse(result, "Expected customer with null date of birth to be invalid");
    }

    @Test
    public void testIsEvenAge_EvenAge() {
        Customer customer = new Customer("Alice", "Smith", LocalDate.of(1990, 1, 1));
        boolean result = CustomerUtil.isEvenAge(customer);
        assertTrue(result, "Expected customer with even age to return true");
    }

    @Test
    public void testIsEvenAge_OddAge() {
        Customer customer = new Customer("Bob", "Johnson", LocalDate.of(1991, 1, 1));
        boolean result = CustomerUtil.isEvenAge(customer);
        assertFalse(result, "Expected customer with odd age to return false");
    }
}