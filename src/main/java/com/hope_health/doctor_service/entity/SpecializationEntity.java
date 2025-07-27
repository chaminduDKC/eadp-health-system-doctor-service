package com.hope_health.doctor_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "specializations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SpecializationEntity {
    @Id
    @Column(name = "specialization_id")
    private String specializationId;

    private String specialization;

}
