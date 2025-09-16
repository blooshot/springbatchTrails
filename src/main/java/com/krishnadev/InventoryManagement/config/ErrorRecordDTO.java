package com.krishnadev.InventoryManagement.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorRecordDTO {
    private String line;
    private String reason;

}