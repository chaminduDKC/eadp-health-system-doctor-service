package com.hope_health.doctor_service.repo;

import com.hope_health.doctor_service.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<DoctorEntity, String> {
    List<DoctorEntity> findBySpecializationContainingIgnoreCase(String specialization);
    List<DoctorEntity> findByHospitalContainingIgnoreCase(String hospital);
    List<DoctorEntity> findByHospitalAndSpecializationContainingIgnoreCase(String hospital, String specialization);
}
