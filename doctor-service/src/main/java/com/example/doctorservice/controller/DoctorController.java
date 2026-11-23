package com.example.doctorservice.controller;


import com.example.doctorservice.dto.ApiResponse;
import com.example.doctorservice.dto.DoctorRequest;
import com.example.doctorservice.dto.DoctorResponse;
import com.example.doctorservice.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<DoctorResponse>> create(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor profile created successfully", doctorService.create(request)));
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getById(id)));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAll(
            @RequestParam(required = false) String specialization) {
        List<DoctorResponse> doctors = (specialization != null && !specialization.isBlank())
                ? doctorService.searchBySpecialization(specialization)
                : doctorService.getAll();
        return ResponseEntity.ok(ApiResponse.success(doctors));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> update(
            @PathVariable Long id, @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Doctor profile updated successfully", doctorService.update(id, request)));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deactivate(@PathVariable Long id) {
        doctorService.deactivate(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deactivated successfully", null));
    }
}
