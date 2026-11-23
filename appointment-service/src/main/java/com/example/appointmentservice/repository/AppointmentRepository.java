package com.example.appointmentservice.repository;


import com.example.appointmentservice.entity.Appointment;
import com.example.appointmentservice.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate appointmentDate);

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusNotIn(
            Long doctorId, LocalDate appointmentDate, List<AppointmentStatus> excludedStatuses);

    boolean existsByDoctorIdAndAppointmentDateAndStartTimeAndStatusNotIn(
            Long doctorId, LocalDate appointmentDate, LocalTime startTime, List<AppointmentStatus> excludedStatuses);
}