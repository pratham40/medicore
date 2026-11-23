package com.example.appointmentservice.dto;

import com.example.appointmentservice.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "Status is required") AppointmentStatus status,
        String notes
) {}