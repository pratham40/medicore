package com.example.doctorservice.service;

import com.example.doctorservice.dto.AvailabilityRequest;
import com.example.doctorservice.dto.AvailabilityResponse;
import com.example.doctorservice.dto.AvailableSlotResponse;
import com.example.doctorservice.dto.LeaveRequest;
import com.example.doctorservice.entity.DoctorAvailability;
import com.example.doctorservice.entity.DoctorLeave;
import com.example.doctorservice.repository.DoctorAvailabilityRepository;
import com.example.doctorservice.repository.DoctorLeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorLeaveRepository leaveRepository;

    @Transactional
    public AvailabilityResponse setWeeklyAvailability(Long doctorId, AvailabilityRequest request) {
        DoctorAvailability availability = DoctorAvailability.builder()
                .doctorId(doctorId)
                .dayOfWeek(request.dayOfWeek())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .slotDurationMinutes(request.slotDurationMinutes())
                .build();

        DoctorAvailability saved = availabilityRepository.save(availability);
        return toResponse(saved);
    }

    public List<AvailabilityResponse> getWeeklyAvailability(Long doctorId) {
        return availabilityRepository.findByDoctorId(doctorId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public void addLeave(Long doctorId, LeaveRequest request) {
        DoctorLeave leave = DoctorLeave.builder()
                .doctorId(doctorId)
                .leaveDate(request.leaveDate())
                .reason(request.reason())
                .build();
        leaveRepository.save(leave);
    }

    public List<AvailableSlotResponse> generateSlotsForDate(Long doctorId, LocalDate date) {
        Optional<DoctorLeave> leave = leaveRepository.findByDoctorIdAndLeaveDate(doctorId, date);
        if (leave.isPresent()) {
            return List.of(); // doctor is on leave — no slots at all
        }

        Optional<DoctorAvailability> template =
                availabilityRepository.findByDoctorIdAndDayOfWeek(doctorId, date.getDayOfWeek());

        if (template.isEmpty()) {
            return List.of(); // doctor doesn't work on this day of week
        }

        DoctorAvailability avail = template.get();
        List<AvailableSlotResponse> slots = new ArrayList<>();

        LocalTime cursor = avail.getStartTime();
        while (cursor.plusMinutes(avail.getSlotDurationMinutes()).compareTo(avail.getEndTime()) <= 0) {
            LocalTime slotEnd = cursor.plusMinutes(avail.getSlotDurationMinutes());
            slots.add(new AvailableSlotResponse(date, cursor, slotEnd, true));
            cursor = slotEnd;
        }

        return slots;
    }

    private AvailabilityResponse toResponse(DoctorAvailability a) {
        return new AvailabilityResponse(
                a.getId(), a.getDoctorId(), a.getDayOfWeek(),
                a.getStartTime(), a.getEndTime(), a.getSlotDurationMinutes()
        );
    }
}