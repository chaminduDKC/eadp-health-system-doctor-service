package com.hope_health.doctor_service.service.impl;

import com.hope_health.doctor_service.config.WebClientConfig;
import com.hope_health.doctor_service.dto.request.DoctorRequestDto;
import com.hope_health.doctor_service.repo.DoctorRepo;
import com.hope_health.doctor_service.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepo doctorRepo;
    private final WebClientConfig webClientConfig;

    @Override
    public void createDoctor(DoctorRequestDto request) {

    }
}
