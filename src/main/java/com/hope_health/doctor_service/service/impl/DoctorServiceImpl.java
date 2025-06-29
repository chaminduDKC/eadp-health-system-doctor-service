package com.hope_health.doctor_service.service.impl;

import com.hope_health.doctor_service.config.WebClientConfig;
import com.hope_health.doctor_service.dto.request.DoctorRequestDto;
import com.hope_health.doctor_service.dto.response.DoctorResponseDto;
import com.hope_health.doctor_service.entity.DoctorEntity;
import com.hope_health.doctor_service.repo.DoctorRepo;
import com.hope_health.doctor_service.service.DoctorService;
import com.hope_health.doctor_service.util.DoctorResponsePaginated;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepo doctorRepo;
    private final WebClientConfig webClientConfig;

    @Override
    public void createDoctor(DoctorRequestDto request) {
        Optional<DoctorEntity> existDoc = doctorRepo.findByEmail(request.getEmail());
        if(existDoc.isPresent()){
            throw new RuntimeException("Doc is already exist");
        }
        try{
            DoctorEntity doctor = DoctorEntity.builder()
                    .city(request.getCity())
                    .name(request.getName())
                    .userId(request.getUserId())
                    .email(request.getEmail())
                    .doctorId(UUID.randomUUID().toString())
                    .specialization(request.getSpecialization())
                    .hospital(request.getHospital())
                    .licenceNo(request.getLicenseNo())
                    .phone(request.getPhoneNumber())
                    .address(request.getAddress())
                    .experience(request.getExperience())
                    .build();
            DoctorEntity saved = doctorRepo.save(doctor);
        } catch (Exception e){
            throw new RuntimeException("Failed to save doctor");
        }
    }

    @Override
    public DoctorResponsePaginated findAllDoctors(String searchText, int page, int size) {

        return DoctorResponsePaginated.builder()
                .dataCount(doctorRepo.countAll(searchText))
                .dataList(doctorRepo.searchAll(searchText, PageRequest.of(page, size)).stream()
                        .map(this::toResponse).toList())
                .build();
    }

    @Override
    public void deleteDoctorById(String doctorId) {
        try{
            DoctorEntity doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found with given id"));

            String userId = doctor.getUserId();
            doctorRepo.deleteById(doctorId);
            webClientConfig.webClient().delete().uri("http://localhost:9090/api/users/delete-user/{userid}", userId)
                    .retrieve().bodyToMono(Boolean.class).block();

            System.out.println("deleted");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private DoctorResponseDto toResponse(DoctorEntity entity){
        return DoctorResponseDto.builder()
                .doctorId(entity.getDoctorId())
                .name(entity.getName())
                .licenceNo(entity.getLicenceNo())
                .address(entity.getAddress())
                .experience(entity.getExperience())
                .phoneNumber(entity.getPhone())
                .specialization(entity.getSpecialization())
                .email(entity.getEmail())
                .hospital(entity.getHospital())
                .build();
    }
}
