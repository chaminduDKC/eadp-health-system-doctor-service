package com.hope_health.doctor_service.service.impl;

import com.hope_health.doctor_service.dto.request.HospitalRequest;
import com.hope_health.doctor_service.dto.response.HospitalResponse;
import com.hope_health.doctor_service.entity.HospitalEntity;
import com.hope_health.doctor_service.repo.HospitalRepo;
import com.hope_health.doctor_service.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepo hospitalRepo;
    @Override
    public HospitalResponse createHospital(HospitalRequest request) {
        try{
            Optional<HospitalEntity> hospital = hospitalRepo.findByHospitalName(request.getHospitalName());

            if(hospital.isEmpty()){
                System.out.println(toResponse(toEntity(request)));
                hospitalRepo.save(toEntity(request));
               return toResponse(toEntity(request));
            } else {
                throw new RuntimeException("This hospital Already exist. Use another name");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean deleteHospital(String hospitalId) {
        Optional<HospitalEntity> entity = hospitalRepo.findById(hospitalId);
        if(entity.isEmpty()){
            throw new RuntimeException("No hospital for this id");
        }
        hospitalRepo.deleteById(hospitalId);
        return true;
    }

    @Override
    public List<String> getAllHospitals(String searchText) {
        List<HospitalEntity> entityList = hospitalRepo.findAll();

        return entityList.stream().map(HospitalEntity::getHospitalName).toList();

    }

    private HospitalEntity toEntity (HospitalRequest request){
        return HospitalEntity.builder()
                .hospitalId(UUID.randomUUID().toString())
                .hospitalName(request.getHospitalName())
                .build();
    }

    private HospitalResponse toResponse (HospitalEntity entity){
        return HospitalResponse.builder()
                .hospitalId(entity.getHospitalId())
                .hospitalName(entity.getHospitalName())
                .build();
    }
}
