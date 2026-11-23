    package com.gl.patientservice.service;

    import com.gl.patientservice.dto.PatientRequest;
    import com.gl.patientservice.dto.PatientResponse;
    import com.gl.patientservice.entity.Patient;
    import com.gl.patientservice.exception.ResourceNotFoundException;
    import com.gl.patientservice.mapper.MapResponse;
    import com.gl.patientservice.repository.PatientRepository;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Service;

    import java.time.Instant;
    import java.util.List;

    @Service
    @RequiredArgsConstructor
    public class PatientService {

        private static final Logger log = LoggerFactory.getLogger(PatientService.class);
        private final PatientRepository patientRepository;

        private final MapResponse mapResponse;

        @Transactional
        public PatientResponse register(PatientRequest request, Long linkingUserId) {

            log.info("In PatientService register");

            Patient patient = Patient.builder()
                    .userId(linkingUserId)
                    .fullName(request.fullName())
                    .dateOfBirth(request.dateOfBirth())
                    .gender(request.gender())
                    .phoneNumber(request.phoneNumber())
                    .email(request.email())
                    .address(request.address())
                    .emergencyContactName(request.emergencyContactName())
                    .emergencyContactPhone(request.emergencyContactPhone())
                    .allergies(request.allergies())
                    .chronicConditions(request.chronicConditions())
                    .build();

            log.info(patient.toString());

            Patient saved = patientRepository.save(patient);
            return mapResponse.toResponse(saved);
        }

        public PatientResponse getById(Long id) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
            return mapResponse.toResponse(patient);
        }

        public List<PatientResponse> getAll() {
            return patientRepository.findAll().stream()
                    .map(mapResponse::toResponse)
                    .toList();
        }

        public List<PatientResponse> searchByName(String name) {
            return patientRepository.findByFullNameContainingIgnoreCase(name).stream()
                    .map(mapResponse::toResponse)
                    .toList();
        }

        public PatientResponse getByPhone(String phone) {
            Patient patient = patientRepository.findByPhoneNumber(phone)
                    .orElseThrow(() -> new ResourceNotFoundException("No patient found with phone: " + phone));
            return mapResponse.toResponse(patient);
        }

        @Transactional
        public PatientResponse update(Long id, PatientRequest request) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

            patient.setFullName(request.fullName());
            patient.setDateOfBirth(request.dateOfBirth());
            patient.setGender(request.gender());
            patient.setPhoneNumber(request.phoneNumber());
            patient.setEmail(request.email());
            patient.setAddress(request.address());
            patient.setEmergencyContactName(request.emergencyContactName());
            patient.setEmergencyContactPhone(request.emergencyContactPhone());
            patient.setAllergies(request.allergies());
            patient.setChronicConditions(request.chronicConditions());
            patient.setUpdatedAt(Instant.now());

            return mapResponse.toResponse(patientRepository.save(patient));
        }

        @Transactional
        public void delete(Long id) {
            if (!patientRepository.existsById(id)) {
                throw new ResourceNotFoundException("Patient not found with id: " + id);
            }
            patientRepository.deleteById(id);
        }



    }
