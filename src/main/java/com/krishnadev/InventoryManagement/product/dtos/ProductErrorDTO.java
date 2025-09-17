package com.krishnadev.InventoryManagement.product.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductErrorDTO {

    private  String title;
    private  String description;
    private  Boolean instock;
    private  int quantity;
    private  double price;
    private String errorMessage;


}
