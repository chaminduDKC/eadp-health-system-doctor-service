package com.hope_health.doctor_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String name;
    private String phone;
    private String specialization;
    private String experience;
    private String hospital;
    private String address;
    private String licenceNo;
    private String city;
}
