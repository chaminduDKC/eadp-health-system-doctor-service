package com.hope_health.doctor_service.controller;

import com.hope_health.doctor_service.dto.request.HospitalRequest;
import com.hope_health.doctor_service.service.HospitalService;
import com.hope_health.doctor_service.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
public class HospitalController {
    private final HospitalService hospitalService;

    @PostMapping("/save-hospital")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StandardResponse> addHospital(@RequestBody HospitalRequest request){
        System.out.println(request);
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(201)
                        .message("Hospital Added Success")
                        .data(hospitalService.createHospital(request))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete-hospital/{hospitalId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StandardResponse> deleteHospital(@PathVariable String hospitalId){
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("Hospital deleted Success")
                        .data(hospitalService.deleteHospital(hospitalId))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/find-all-hospitals")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StandardResponse> findAllHospitals(@RequestParam String searchText){
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("Hospitals retrieved Success")
                        .data(hospitalService.getAllHospitals(searchText))
                        .build(),
                HttpStatus.OK
        );
    }
}
