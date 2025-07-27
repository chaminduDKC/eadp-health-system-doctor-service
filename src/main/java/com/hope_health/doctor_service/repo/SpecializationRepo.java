package com.hope_health.doctor_service.repo;

import com.hope_health.doctor_service.entity.SpecializationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializationRepo extends JpaRepository<SpecializationEntity, String> {
     Optional<SpecializationEntity> findBySpecialization(String specialization);

     @Query(nativeQuery = true, value = "SELECT * FROM specializations WHERE specialization LIKE %?1%")
     List<SpecializationEntity> searchAll(String searchText);
}
