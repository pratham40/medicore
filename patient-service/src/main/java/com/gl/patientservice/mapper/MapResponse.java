package com.gl.patientservice.mapper;

import com.gl.patientservice.dto.PatientResponse;
import com.gl.patientservice.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class MapResponse {
    public PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .allergies(patient.getAllergies())
                .chronicConditions(patient.getChronicConditions())
                .createdAt(patient.getCreatedAt())
                .build();


    }
}
