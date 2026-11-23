package com.example.doctorservice.repository;


import com.example.doctorservice.entity.DoctorLeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, Long> {
    List<DoctorLeave> findByDoctorId(Long doctorId);
    Optional<DoctorLeave> findByDoctorIdAndLeaveDate(Long doctorId, LocalDate leaveDate);
}