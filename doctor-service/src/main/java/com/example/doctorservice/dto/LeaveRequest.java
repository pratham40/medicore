package com.example.doctorservice.dto;


import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LeaveRequest(
        @NotNull(message = "Leave date is required") LocalDate leaveDate,
        String reason
) {}