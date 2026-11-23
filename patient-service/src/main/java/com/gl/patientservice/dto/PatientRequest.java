package com.gl.patientservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record PatientRequest(

        @NotBlank(message = "Full name is required")
        String fullName,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Gender is required")
        String gender,

        @NotBlank(message = "Phone number is required")
        String phoneNumber,

        String email,
        String address,
        String emergencyContactName,
        String emergencyContactPhone,
        String allergies,
        String chronicConditions
) {}