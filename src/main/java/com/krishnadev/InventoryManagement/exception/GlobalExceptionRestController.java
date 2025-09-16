package com.krishnadev.InventoryManagement.exception;

import com.krishnadev.InventoryManagement.commons.DTO.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
@Order(1)
public class GlobalExceptionRestController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

//        log.info("Exception: ", ex.getBindingResult().toString());

        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setValidationErrors(validationErrors);
        errorResponseDTO.setApiPath(request.getDescription(false));
        errorResponseDTO.setErrorCode(HttpStatus.BAD_REQUEST);
        errorResponseDTO.setErrorMessage("Validation Error, Check and Fix Errors");
        errorResponseDTO.setErrorTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SS")));
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other uncaught exceptions.
     * This method provides a generic error response for all other exceptions.
     *
     * @param exception the exception to handle
     * @param webRequest the current web request
     * @return a ResponseEntity with a custom error body and HTTP status code
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception exception, WebRequest webRequest) {
//        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
//                getPathFromRequest(webRequest),
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                exception.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    /**
     * Helper method to get the request path from a WebRequest.
     * @param request The WebRequest object.
     * @return The request URI as a String.
     */
//    private String getPathFromRequest(WebRequest request) {
//        if (request instanceof HttpServletRequest) {
//            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//            return httpServletRequest.getRequestURI();
//        }
//        return "unknown";
//    }


//    @ExceptionHandler(ValidationException.class)
//    public ResponseEntity<ErrorResponseDTO> handleValidationException( ValidationException ex, WebRequest request) {
//
//        Map<String, String> validationErrors = new HashMap<>();
//        // Define the custom date-time format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//
//        // Get all constraint violations and collect their messages
//        String errorMessage = ex.getConstraintViolations().stream()
//                .map(ConstraintViolation::getMessage)
//                .collect(Collectors.joining(", "));
//
//        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
//        errorResponseDTO.setValidationErrors(errorMessage);
//        errorResponseDTO.setApiPath(request.getDescription(false));
//        errorResponseDTO.setErrorCode(HttpStatus.BAD_REQUEST);
//        errorResponseDTO.setErrorMessage(errorMessage);
//        errorResponseDTO.setErrorTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SS")));
//
//        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
//    }


}//EC
