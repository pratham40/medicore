package com.example.doctorservice.dto;


import java.math.BigDecimal;

public record DoctorResponse(
        Long id,
        Long userId,
        String fullName,
        String specialization,
        String department,
        BigDecimal consultationFee,
        String qualification,
        Integer yearsOfExperience,
        String phoneNumber,
        boolean active
) {}