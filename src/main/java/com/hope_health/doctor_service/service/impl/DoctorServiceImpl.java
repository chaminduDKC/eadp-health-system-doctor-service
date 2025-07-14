package com.hope_health.doctor_service.service.impl;

import com.hope_health.doctor_service.config.WebClientConfig;
import com.hope_health.doctor_service.dto.request.DoctorRequestDto;
import com.hope_health.doctor_service.dto.request.RecentActivityRequest;
import com.hope_health.doctor_service.dto.request.UserUpdateRequest;
import com.hope_health.doctor_service.dto.response.DoctorResponseDto;
import com.hope_health.doctor_service.entity.DoctorEntity;
import com.hope_health.doctor_service.repo.DoctorRepo;
import com.hope_health.doctor_service.service.DoctorService;
import com.hope_health.doctor_service.util.DoctorResponsePaginated;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.LocalDateTime;
import java.util.*;
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

            RecentActivityRequest activityRequest = RecentActivityRequest.builder()
                    .action(request.getName() +" added as a Doctor")
                    .dateTime(LocalDateTime.now())
                    .description("No Description")
                    .build();

            webClientConfig.webClient().post().uri("http://localhost:9094/api/recent-activities/create-activity")
                    .bodyValue(activityRequest)
                    .retrieve()
                    .bodyToMono(RecentActivityRequest.class)
                    .block();

        } catch (WebClientException e){
            throw new RuntimeException("Doctor Saved.Failed to connect with recent activity service " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Doctor save failed");
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
            try {
                webClientConfig.webClient().delete().uri("http://localhost:9090/api/users/delete-user/{userid}", userId)
                        .retrieve().bodyToMono(Boolean.class).block();
            } catch (WebClientException e) {
                throw new RuntimeException("Failed to delete from user service  " +e.getMessage());
            }

            try {
                webClientConfig.webClient().delete().uri("http://localhost:9093/api/bookings/delete-booking-by-doctor/{doctorId}", doctorId)
                        .retrieve().bodyToMono(Boolean.class).block();
            } catch (WebClientException e) {
                throw new RuntimeException("Failed to delete from booking service  " +e.getMessage());
            }

            System.out.println("deleted");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public DoctorResponseDto updateDoctor(UserUpdateRequest requestDto, String doctorId) {
        Optional<DoctorEntity> doctorEntity = doctorRepo.findById(doctorId);
        if(doctorEntity.isPresent()){
            DoctorEntity doctor = doctorEntity.get();
            doctor.setAddress(requestDto.getAddress());
            doctor.setCity(requestDto.getCity());
            doctor.setName(requestDto.getName());
            doctor.setExperience(requestDto.getExperience());
            doctor.setEmail(requestDto.getEmail());
            doctor.setHospital(requestDto.getHospital());
            doctor.setPhone(requestDto.getPhone());
            doctor.setSpecialization(requestDto.getSpecialization());
            doctor.setLicenceNo(requestDto.getLicenceNo());

            doctorRepo.save(doctor);
            String userId = doctor.getUserId();

            UserUpdateRequest updateRequest = new UserUpdateRequest();
            updateRequest.setName(doctor.getName());
            updateRequest.setEmail(doctor.getEmail());
            try {
                webClientConfig.webClient().put().uri("http://localhost:9090/api/users/update-user/{userid}", userId)
                        .bodyValue(updateRequest)
                        .retrieve().bodyToMono(Void.class).block();
            } catch (Exception e){
                System.out.println("failed to connect user service");
                return null;
            }
            return toResponse(doctor);
        }
        throw new RuntimeException("doctor is not available for this id "+ doctorId);
    }

    @Override
    public long countAll() {
        return doctorRepo.countAll("");
    }

    private DoctorResponseDto toResponse(DoctorEntity entity){
        return DoctorResponseDto.builder()
                .city(entity.getCity())
                .doctorId(entity.getDoctorId())
                .name(entity.getName())
                .licenceNo(entity.getLicenceNo())
                .address(entity.getAddress())
                .experience(entity.getExperience())
                .phoneNumber(entity.getPhone())
                .specialization(entity.getSpecialization())
                .email(entity.getEmail())
                .hospital(entity.getHospital())
                .userId(entity.getUserId())
                .build();
    }
}
