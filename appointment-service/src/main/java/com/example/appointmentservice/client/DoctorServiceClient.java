package com.example.appointmentservice.client;


import com.example.appointmentservice.dto.ApiResponse;
import com.example.appointmentservice.dto.AvailableSlotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;


@FeignClient(name = "DOCTOR-SERVICE")
public interface DoctorServiceClient {

    @GetMapping("/api/v1/doctors/{doctorId}/availability/slots")
    ApiResponse<List<AvailableSlotResponse>> getAvailableSlots(
            @PathVariable("doctorId") Long doctorId,
            @RequestParam("date") LocalDate date
    );
}
