package com.example.doctorservice.controller;

import com.example.doctorservice.dto.*;
import com.example.doctorservice.service.AvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors/{doctorId}/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<AvailabilityResponse>> setWeekly(
            @PathVariable Long doctorId, @Valid @RequestBody AvailabilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Weekly availability added", availabilityService.setWeeklyAvailability(doctorId, request)));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AvailabilityResponse>>> getWeekly(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponse.success(availabilityService.getWeeklyAvailability(doctorId)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PostMapping("/leave")
    public ResponseEntity<ApiResponse<Void>> addLeave(
            @PathVariable Long doctorId, @Valid @RequestBody LeaveRequest request) {
        availabilityService.addLeave(doctorId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Leave added successfully", null));
    }

    // Used by React (booking screen) and later by Voice-Agent-Service
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/slots")
    public ResponseEntity<ApiResponse<List<AvailableSlotResponse>>> getSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(availabilityService.generateSlotsForDate(doctorId, date)));
    }
}