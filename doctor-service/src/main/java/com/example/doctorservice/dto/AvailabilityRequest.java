package com.example.doctorservice.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilityRequest(
        @NotNull(message = "Day of week is required") DayOfWeek dayOfWeek,
        @NotNull(message = "Start time is required") LocalTime startTime,
        @NotNull(message = "End time is required") LocalTime endTime,
        @NotNull(message = "Slot duration is required") @Min(value = 5, message = "Slot duration must be at least 5 minutes") Integer slotDurationMinutes
) {}