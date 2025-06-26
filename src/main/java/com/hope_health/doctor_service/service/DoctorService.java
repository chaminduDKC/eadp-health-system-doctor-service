package com.hope_health.doctor_service.service;

import com.hope_health.doctor_service.dto.request.DoctorRequestDto;

public interface DoctorService {
    void createDoctor(DoctorRequestDto request);
}
