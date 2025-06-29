package com.hope_health.doctor_service.util;

import com.hope_health.doctor_service.dto.response.DoctorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponsePaginated {
    private long dataCount;
    private List<DoctorResponseDto> dataList;
}
