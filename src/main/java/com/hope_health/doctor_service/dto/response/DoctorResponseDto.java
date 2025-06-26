package com.hope_health.doctor_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorResponseDto {
    private String phoneNumber;
    private String specialization;
    private String experience;
    private String hospital;
    private String address;
}
