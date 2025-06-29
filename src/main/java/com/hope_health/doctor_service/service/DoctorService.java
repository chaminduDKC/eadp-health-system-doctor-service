package com.hope_health.doctor_service.service;

import com.hope_health.doctor_service.dto.request.DoctorRequestDto;
import com.hope_health.doctor_service.dto.response.DoctorResponseDto;
import com.hope_health.doctor_service.util.DoctorResponsePaginated;

public interface DoctorService {
    void createDoctor(DoctorRequestDto request);
    DoctorResponsePaginated findAllDoctors(String searchText, int page, int size);

     void deleteDoctorById( String doctorId);
}
