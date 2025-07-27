package com.hope_health.doctor_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "hospitals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HospitalEntity {

    @Id
    @Column(name = "hospital_id")
    private String hospitalId;

    @Column(name = "hospital_name")
    private String hospitalName;


}
