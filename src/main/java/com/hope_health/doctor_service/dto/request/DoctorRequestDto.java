package com.hope_health.doctor_service.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorRequestDto {
    private String userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String experience;
    private String hospital;
    private String address;
    private String licenseNo;
    private String city;
}
