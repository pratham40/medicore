package com.example.appointmentservice.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public record AvailableSlotResponse(
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        boolean available
) {}