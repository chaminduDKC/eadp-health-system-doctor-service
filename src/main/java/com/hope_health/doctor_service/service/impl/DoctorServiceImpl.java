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
import com.hope_health.doctor_service.util.StandardResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.LocalDate;
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
        System.out.println(request);
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
                    .licenceNo(request.getLicenceNo())
                    .phone(request.getPhoneNumber())
                    .address(request.getAddress())
                    .experience(request.getExperience())
                    .build();
            DoctorEntity saved = doctorRepo.save(doctor);


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
    @Transactional
    public void deleteDoctorById(String doctorId, String userId) {
        System.out.println("Doctor id is "+doctorId);
        DoctorEntity doctor = doctorRepo.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with given id"));


        // 1. Delete doctor from doctorRepo
        try {

            doctorRepo.deleteById(doctorId);

            RecentActivityRequest activityRequest = RecentActivityRequest.builder()
                    .action(doctor.getName() + " user deleted by admin : role [doctor]")
                    .dateTime(LocalDateTime.now())
                    .description("From doctor service")
                    .build();

            webClientConfig.webClient()
                    .post()
                    .uri("http://localhost:9094/api/recent-activities/create-activity")
                    .bodyValue(activityRequest)
                    .retrieve()
                    .bodyToMono(RecentActivityRequest.class)
                    .block();
        } catch (WebClientException e) {
            System.out.println("Failed to connect with recent service");

        } catch (Exception e) {
            System.out.println("Failed to delete doctor from repo: " + e.getMessage());
        }


        //
        // 2. Delete user from user service
        try {
            webClientConfig.webClient()
                    .delete()
                    .uri("http://localhost:9090/api/users/delete-user/{userid}", userId)
                    .retrieve()
                    .bodyToMono(StandardResponse.class)
                    .block();
        } catch (WebClientException e) {
            System.out.println("Failed to delete from user service: " + e.getMessage());
        }

        // 3. Record recent activity


        // 4. Delete doctor’s bookings from booking service
        try {
            webClientConfig.webClient()
                    .delete()
                    .uri("http://localhost:9093/api/bookings/delete-booking-by-doctor/{doctorId}", doctorId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

        } catch (WebClientException e) {
            System.out.println("Failed to delete from booking service : " + e.getMessage());
        }

        System.out.println("Delete flow completed.");
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
            doctor.setHospital(requestDto.getHospital());
            doctor.setPhone(requestDto.getPhone());
            doctor.setSpecialization(requestDto.getSpecialization());
            doctor.setLicenceNo(requestDto.getLicenceNo());

            String userId = doctor.getUserId();
            try {
                doctorRepo.save(doctor);

                RecentActivityRequest activityRequest = RecentActivityRequest.builder()
                        .action(doctor.getName() + " user updated : role [doctor]")
                        .dateTime(LocalDateTime.now())
                        .description("From doctor service")
                        .build();

                webClientConfig.webClient()
                        .post()
                        .uri("http://localhost:9094/api/recent-activities/create-activity")
                        .bodyValue(activityRequest)
                        .retrieve()
                        .bodyToMono(RecentActivityRequest.class)
                        .block();

            } catch (WebClientException e){
                System.out.println("Failed to connect with recent service");
            }

            catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                UserUpdateRequest updateRequest = new UserUpdateRequest();
                updateRequest.setName(doctor.getName());

                webClientConfig.webClient().put().uri("http://localhost:9090/api/users/update-user/{userid}", userId)
                        .bodyValue(updateRequest)
                        .retrieve().bodyToMono(Void.class).block();



            } catch (WebClientException e){
                System.out.println("failed to connect user service ");
            }
            catch (Exception e){
                System.out.println("Something went wrong");
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

    @Override
    public List<DoctorResponseDto> findDoctorBySpecialization(String specialization) {
        System.out.println("specialization: " + specialization);
        return doctorRepo.findDoctorBySpecialization(specialization).stream().map(this::toResponse).toList();

    }

    @Override
    public Boolean updateEmail(String email, String userId) {
        Optional<DoctorEntity> doctorEntity = doctorRepo.findByUserId(userId);
        if(doctorEntity.isPresent()){
            DoctorEntity doctor = doctorEntity.get();
            doctor.setEmail(email);
            doctorRepo.save(doctor);
        } else {
            throw new RuntimeException("doctor is not available for this id "+ userId);
        }
        return true;
    }

    @Override
    public DoctorResponseDto findDoctorByUserId(Jwt jwt) {
        if(jwt != null && jwt.getSubject() != null) {
            String userId = jwt.getSubject();
            Optional<DoctorEntity> doctorEntity = doctorRepo.findByUserId(userId);
            if(doctorEntity.isPresent()){
                return toResponse(doctorEntity.get());
            } else {
                throw new RuntimeException("Doctor not found for this user id");
            }
        }
        throw new RuntimeException("Invalid JWT token or userId not found in token");

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
