package com.hope_health.doctor_service.service;


import com.hope_health.doctor_service.dto.request.SpecializationRequest;
import com.hope_health.doctor_service.dto.response.SpecializationResponse;

import java.util.List;

public interface SpecializationService {
    SpecializationResponse createSpecialization(SpecializationRequest request);

    Boolean deleteSpecialization(String specializationId);

    List<SpecializationResponse> getAllSpecializations(String searchText);
}
