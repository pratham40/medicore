package com.example.appointmentservice.dto;



import com.example.appointmentservice.enums.BookingSource;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        @NotNull(message = "Patient ID is required") Long patientId,
        @NotNull(message = "Doctor ID is required") Long doctorId,
        @NotNull(message = "Appointment date is required") LocalDate appointmentDate,
        @NotNull(message = "Start time is required") LocalTime startTime,
        @NotNull(message = "End time is required") LocalTime endTime,
        @NotNull(message = "Booking source is required") BookingSource source,
        String reasonForVisit
) {}