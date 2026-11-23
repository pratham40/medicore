package com.example.doctorservice.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DoctorRequest(
        @NotNull(message = "User ID is required") Long userId,
        @NotBlank(message = "Full name is required") String fullName,
        @NotBlank(message = "Specialization is required") String specialization,
        @NotBlank(message = "Department is required") String department,
        @NotNull(message = "Consultation fee is required") @DecimalMin(value = "0.0", message = "Fee cannot be negative") BigDecimal consultationFee,
        String qualification,
        Integer yearsOfExperience,
        String phoneNumber
) {}