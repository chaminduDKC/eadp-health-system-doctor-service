package com.hope_health.doctor_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "doctors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DoctorEntity {
    @Id
    @Value("doctor_id")
    private String doctorId;

    private String specialization;

    @Value("licence_no")
    private String licenceNo;

    private String hospital;

    @Value("user_id")
    private String userId; // to link with user service
}
