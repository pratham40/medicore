package com.gl.patientservice.dto;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record PatientResponse(
        Long id,
        String fullName,
        LocalDate dateOfBirth,
        String gender,
        String phoneNumber,
        String email,
        String address,
        String emergencyContactName,
        String emergencyContactPhone,
        String allergies,
        String chronicConditions,
        Instant createdAt
) {}