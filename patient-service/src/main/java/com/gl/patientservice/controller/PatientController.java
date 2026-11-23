package com.gl.patientservice.controller;

import com.gl.patientservice.dto.PatientRequest;
import com.gl.patientservice.dto.PatientResponse;
import com.gl.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private static final Logger log = LoggerFactory.getLogger(PatientController.class);
    private final PatientService patientService;

    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    @PostMapping
    public ResponseEntity<PatientResponse> register(@Valid @RequestBody PatientRequest request,
                                                    @RequestHeader(value = "X-User-Id",required = false) Long userId,
                                                    @RequestHeader(value = "X-User-Role",required = false) String role
                                                    ) {
        log.info("register request");

        Long linkingUserId = "PATIENT".equals(role) ? userId : null;
        PatientResponse response = patientService.register(request,linkingUserId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll(
            @RequestParam(required = false) String name) {
        List<PatientResponse> patients = (name != null && !name.isBlank()) ? patientService.searchByName(name) : patientService.getAll();
        return ResponseEntity.ok(patients);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @GetMapping("/by-phone/{phone}")
    public ResponseEntity<PatientResponse> getByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(patientService.getByPhone(phone));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> update(
            @PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Patient deleted successfully");
    }
}
