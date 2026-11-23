package com.gl.patientservice.repository;

import com.gl.patientservice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    List<Patient> findByFullNameContainingIgnoreCase(String name);
}