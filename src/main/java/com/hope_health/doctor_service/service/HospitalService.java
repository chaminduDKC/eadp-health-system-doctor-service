package com.hope_health.doctor_service.service;

import com.hope_health.doctor_service.dto.request.HospitalRequest;
import com.hope_health.doctor_service.dto.response.HospitalResponse;

import java.util.List;

public interface HospitalService {
    HospitalResponse createHospital(HospitalRequest request);

    Boolean deleteHospital(String hospitalId);

    List<String> getAllHospitals(String searchText);
}
