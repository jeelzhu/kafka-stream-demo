package com.example.demo.entity;

import java.time.LocalDate;

public record Customer (String firstName, String lastName, LocalDate dateOfBirth) {

}
