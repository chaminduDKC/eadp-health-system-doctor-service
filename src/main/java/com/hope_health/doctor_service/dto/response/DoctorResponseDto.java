package com.hope_health.doctor_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorResponseDto {
    private String doctorId;
    private String city;
    private String name;
    private String licenceNo;
    private String phoneNumber;
    private String email;
    private String specialization;
    private String experience;
    private String hospital;
    private String address;
    private String userId;
}
