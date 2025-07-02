package com.hope_health.doctor_service.controller;

import com.hope_health.doctor_service.config.WebClientConfig;
import com.hope_health.doctor_service.dto.request.DoctorRequestDto;
import com.hope_health.doctor_service.dto.request.UserUpdateRequest;
import com.hope_health.doctor_service.service.DoctorService;
import com.hope_health.doctor_service.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final WebClientConfig webClientConfig;
    private final DoctorService doctorService;

    @PostMapping("/create-doctor")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StandardResponse> createDoctor(@RequestBody DoctorRequestDto request) {
        doctorService.createDoctor(request);
        return ResponseEntity.ok(
                StandardResponse.builder()
                        .code(201)
                        .message("Doctor created successfully")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/find-doctor/{doctorId}")
    public ResponseEntity<StandardResponse> getDocById(@PathVariable String doctorId ){
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("Doctor retrived ")
                        .data(null)
                        .build(),
                HttpStatus.OK
        );
    }

    @PutMapping("/update-doctor/{doctorId}")
    public ResponseEntity<StandardResponse> updateDoctor(@RequestBody UserUpdateRequest requestDto, @PathVariable String doctorId){
        System.out.println(requestDto);
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("Doctor Updated successfully")
                        .data(doctorService.updateDoctor(requestDto, doctorId))
                        .build(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete-doctor/{doctorId}")
    public ResponseEntity<StandardResponse> deleteById(@PathVariable String doctorId ){
        doctorService.deleteDoctorById(doctorId);
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("Doctor deleted with id "+doctorId)
                        .data(null)
                        .build(),
                HttpStatus.OK
        );
    }


    @GetMapping("/find-all-doctors")
    public ResponseEntity<StandardResponse> findAllDoctors(@RequestParam String searchText, @RequestParam int page, @RequestParam int size){
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("All doctors retrieved")
                        .data(doctorService.findAllDoctors(searchText, page, size))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/search")
    public ResponseEntity<StandardResponse> searchDoctors(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String hospital
    ) {
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("searching docs")
                        .data(null)
                        .build(),
                HttpStatus.FOUND
        );
    }



    @GetMapping("/count-all")
    public ResponseEntity<StandardResponse> countAll(){
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .message("All doctor count")
                        .code(200)
                        .data(doctorService.countAll())
                        .build(),
                HttpStatus.OK
        );
    }


    @GetMapping("/test-web-client")
    public String test(){
        return webClientConfig.webClient().get().uri("http://localhost:9090/api/users/test")
                .retrieve().bodyToMono(String.class).block();
    }
}
