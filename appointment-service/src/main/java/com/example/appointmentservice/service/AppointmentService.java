package com.example.appointmentservice.service;
import com.example.appointmentservice.client.DoctorServiceClient;
import com.example.appointmentservice.dto.*;
import com.example.appointmentservice.entity.Appointment;
import com.example.appointmentservice.enums.AppointmentStatus;
import com.example.appointmentservice.exception.ResourceNotFoundException;
import com.example.appointmentservice.exception.SlotUnavailableException;
import com.example.appointmentservice.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private static final List<AppointmentStatus> EXCLUDED_FOR_CONFLICT = List.of(
            AppointmentStatus.CANCELLED, AppointmentStatus.NO_SHOW
    );

    private final AppointmentRepository appointmentRepository;
    private final DoctorServiceClient doctorServiceClient;

    /**
     * Books an appointment. Two checks happen before saving:
     * 1. The slot must actually exist in Doctor-Service's generated
     *    availability for that date (weekly template minus leave).
     * 2. No other active appointment already occupies that exact
     *    doctor+date+startTime combination (DB-level conflict check).
     *
     * Used identically by the web frontend, receptionist console, and
     * (in Phase 4) Voice-Agent-Service — same entry point for all three.
     */
    @Transactional
    public AppointmentResponse book(AppointmentRequest request) {
        validateSlotExistsInDoctorSchedule(request);
        validateNoConflict(request);

        Appointment appointment = Appointment.builder()
                .patientId(request.patientId())
                .doctorId(request.doctorId())
                .appointmentDate(request.appointmentDate())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .status(AppointmentStatus.CONFIRMED)
                .source(request.source())
                .reasonForVisit(request.reasonForVisit())
                .build();

        return toResponse(appointmentRepository.save(appointment));
    }

    private void validateSlotExistsInDoctorSchedule(AppointmentRequest request) {
        ApiResponse<List<AvailableSlotResponse>> response =
                doctorServiceClient.getAvailableSlots(request.doctorId(), request.appointmentDate());

        boolean slotExists = response.getData().stream()
                .anyMatch(slot -> slot.startTime().equals(request.startTime())
                        && slot.endTime().equals(request.endTime())
                        && slot.available());

        if (!slotExists) {
            throw new SlotUnavailableException(
                    "Requested slot is not part of the doctor's available schedule for this date");
        }
    }

    private void validateNoConflict(AppointmentRequest request) {
        boolean alreadyBooked = appointmentRepository
                .existsByDoctorIdAndAppointmentDateAndStartTimeAndStatusNotIn(
                        request.doctorId(), request.appointmentDate(), request.startTime(), EXCLUDED_FOR_CONFLICT);

        if (alreadyBooked) {
            throw new SlotUnavailableException("This slot has just been booked by someone else — please pick another");
        }
    }

    public AppointmentResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<AppointmentResponse> getByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream().map(this::toResponse).toList();
    }

    public List<AppointmentResponse> getByDoctorAndDate(Long doctorId, java.time.LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, StatusUpdateRequest request) {
        Appointment appointment = findOrThrow(id);
        appointment.setStatus(request.status());
        if (request.notes() != null) {
            appointment.setNotes(request.notes());
        }
        appointment.setUpdatedAt(Instant.now());
        return toResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse cancel(Long id) {
        Appointment appointment = findOrThrow(id);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setUpdatedAt(Instant.now());
        return toResponse(appointmentRepository.save(appointment));
    }

    private Appointment findOrThrow(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(), a.getPatientId(), a.getDoctorId(), a.getAppointmentDate(),
                a.getStartTime(), a.getEndTime(), a.getStatus(), a.getSource(),
                a.getReasonForVisit(), a.getNotes(), a.getCreatedAt()
        );
    }
}