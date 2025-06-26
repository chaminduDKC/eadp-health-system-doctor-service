package com.hope_health.doctor_service.util;

import lombok.*;

@Data
@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class StandardResponse {
    private int code;
    private String message;
    private Object data;
}
