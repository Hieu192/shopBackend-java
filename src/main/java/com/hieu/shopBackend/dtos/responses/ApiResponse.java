package com.hieu.shopBackend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private int code;
    private boolean success = false;
    private String message;
    private T result;
    private List<String> errors;
    private String error;
    private Long id;
}
