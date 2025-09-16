package com.krishnadev.InventoryManagement.config;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private String line;
    private String reason;

    public ValidationException(String line, String reason) {
        super(reason);
        this.line = line;
        this.reason = reason;
    }
}
