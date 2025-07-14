package com.hope_health.doctor_service.repo;

import com.hope_health.doctor_service.dto.response.DoctorResponseDto;
import com.hope_health.doctor_service.entity.DoctorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<DoctorEntity, String> {
    Optional<DoctorEntity> findByEmail(String email);
    List<DoctorEntity> findBySpecializationContainingIgnoreCase(String specialization);
    List<DoctorEntity> findByHospitalContainingIgnoreCase(String hospital);
    List<DoctorEntity> findByHospitalAndSpecializationContainingIgnoreCase(String hospital, String specialization);

    @Query(nativeQuery = true, value = "SELECT COUNT(doctor_id) FROM doctors WHERE email LIKE %?1%")
    long countAll(String searchText);

    @Query(nativeQuery = true, value = "SELECT * FROM doctors WHERE email LIKE %?1% OR name LIKE %?1% OR specialization LIKE %?1%")
    Page<DoctorEntity> searchAll(String searchText, Pageable pageable);
}
