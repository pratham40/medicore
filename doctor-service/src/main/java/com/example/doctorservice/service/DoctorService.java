package com.example.doctorservice.service;


import com.example.doctorservice.dto.DoctorRequest;
import com.example.doctorservice.dto.DoctorResponse;
import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.exception.ResourceNotFoundException;
import com.example.doctorservice.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        Doctor doctor = Doctor.builder()
                .userId(request.userId())
                .fullName(request.fullName())
                .specialization(request.specialization())
                .department(request.department())
                .consultationFee(request.consultationFee())
                .qualification(request.qualification())
                .yearsOfExperience(request.yearsOfExperience())
                .phoneNumber(request.phoneNumber())
                .active(true)
                .build();

        return toResponse(doctorRepository.save(doctor));
    }

    public DoctorResponse getById(Long id) {
        return toResponse(findDoctorOrThrow(id));
    }

    public List<DoctorResponse> getAll() {
        return doctorRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public List<DoctorResponse> searchBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = findDoctorOrThrow(id);

        doctor.setFullName(request.fullName());
        doctor.setSpecialization(request.specialization());
        doctor.setDepartment(request.department());
        doctor.setConsultationFee(request.consultationFee());
        doctor.setQualification(request.qualification());
        doctor.setYearsOfExperience(request.yearsOfExperience());
        doctor.setPhoneNumber(request.phoneNumber());

        return toResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void deactivate(Long id) {
        Doctor doctor = findDoctorOrThrow(id);
        doctor.setActive(false);
        doctorRepository.save(doctor);
    }

    Doctor findDoctorOrThrow(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    private DoctorResponse toResponse(Doctor d) {
        return new DoctorResponse(
                d.getId(), d.getUserId(), d.getFullName(), d.getSpecialization(),
                d.getDepartment(), d.getConsultationFee(), d.getQualification(),
                d.getYearsOfExperience(), d.getPhoneNumber(), d.isActive()
        );
    }
}