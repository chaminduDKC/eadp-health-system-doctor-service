package com.hope_health.doctor_service.repo;

import com.hope_health.doctor_service.entity.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HospitalRepo extends JpaRepository<HospitalEntity, String> {
    Optional<HospitalEntity> findByHospitalName(String hospitalName);
}
