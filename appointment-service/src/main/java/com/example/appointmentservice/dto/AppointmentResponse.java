package com.example.appointmentservice.dto;

import com.example.appointmentservice.enums.AppointmentStatus;
import com.example.appointmentservice.enums.BookingSource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        Long doctorId,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        AppointmentStatus status,
        BookingSource source,
        String reasonForVisit,
        String notes,
        Instant createdAt
) {}