package com.krishnadev.InventoryManagement.commons.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

    private  String apiPath;
    private HttpStatus errorCode;
    private  String errorMessage;
    private Map<String, String> validationErrors;
    private String errorTime;
}
