package com.hope_health.doctor_service.controller;

import com.hope_health.doctor_service.dto.request.SpecializationRequest;
import com.hope_health.doctor_service.service.SpecializationService;
import com.hope_health.doctor_service.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/specializations")
@RequiredArgsConstructor
public class SpecializationController {

    private final SpecializationService specializationService;

    @PostMapping("/create-specialization")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StandardResponse> addSpecialization(@RequestBody SpecializationRequest request){
        System.out.println(request);
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(201)
                        .message("Specialization Added Success")
                        .data(specializationService.createSpecialization(request))
                        .build(),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete-specialization/{specializationId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StandardResponse> deleteSpecialization(@PathVariable String specializationId){
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("specialization deleted Success")
                        .data(specializationService.deleteSpecialization(specializationId))
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/find-all-specializations")

    public ResponseEntity<StandardResponse> findAllSpecialization(@RequestParam String searchText){
        return new ResponseEntity<>(
                StandardResponse.builder()
                        .code(200)
                        .message("Specializations are retrieved Success")
                        .data(specializationService.getAllSpecializations(searchText))
                        .build(),
                HttpStatus.OK
        );
    }
}
