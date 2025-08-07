package com.hope_health.doctor_service.service;

import com.hope_health.doctor_service.dto.request.DoctorRequestDto;
import com.hope_health.doctor_service.dto.request.UserUpdateRequest;
import com.hope_health.doctor_service.dto.response.DoctorResponseDto;
import com.hope_health.doctor_service.util.DoctorResponsePaginated;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    void createDoctor(DoctorRequestDto request);
    DoctorResponsePaginated findAllDoctors(String searchText, int page, int size);

     void deleteDoctorById( String doctorId, String userId);

    DoctorResponseDto updateDoctor(UserUpdateRequest requestDto, String doctorId);

    long countAll();

    List<DoctorResponseDto> findDoctorBySpecialization(String specialization);

    Boolean updateEmail(String email, String userId);

    DoctorResponseDto findDoctorByUserId(Jwt jwt);
}
