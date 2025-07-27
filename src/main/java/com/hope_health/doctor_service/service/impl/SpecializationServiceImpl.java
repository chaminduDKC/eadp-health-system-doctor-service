package com.hope_health.doctor_service.service.impl;

import com.hope_health.doctor_service.dto.request.SpecializationRequest;
import com.hope_health.doctor_service.dto.response.SpecializationResponse;
import com.hope_health.doctor_service.entity.SpecializationEntity;
import com.hope_health.doctor_service.repo.SpecializationRepo;
import com.hope_health.doctor_service.service.SpecializationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {
    private final SpecializationRepo specializationRepo;


    @Override
    public SpecializationResponse createSpecialization(SpecializationRequest request) {
        Optional<SpecializationEntity> entity = specializationRepo.findBySpecialization(request.getSpecialization());
        if(entity.isPresent()){
            throw new RuntimeException("This specialization is already exist");
        }

        specializationRepo.save(toEntity(request));
        return toResponse(toEntity(request));
    }

    @Override
    public Boolean deleteSpecialization(String specializationId) {
        Optional<SpecializationEntity> entity = specializationRepo.findById(specializationId);
        if(entity.isPresent()){
            specializationRepo.deleteById(specializationId);
            return true;
        }
        throw new RuntimeException("No specialization does exist with given id");
    }

    @Override
    public List<SpecializationResponse> getAllSpecializations(String searchText) {

        return specializationRepo.searchAll(searchText).stream().map(this::toResponse).toList();
    }

    private SpecializationResponse toResponse (SpecializationEntity entity){
        if(entity == null){
            return null;
        }
        return SpecializationResponse.builder()
                .specialization(entity.getSpecialization())
                .specializationId(entity.getSpecializationId())
                .build();
    }

    private SpecializationEntity toEntity (SpecializationRequest request){
        if(request == null){
            return null;
        }
        return SpecializationEntity.builder()
                .specializationId(UUID.randomUUID().toString())
                .specialization(request.getSpecialization())
                .build();
    }
}
