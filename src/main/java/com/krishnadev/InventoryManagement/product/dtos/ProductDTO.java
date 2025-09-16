package com.krishnadev.InventoryManagement.product.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO implements Serializable {

        @JsonProperty( "product_id")
        private  Long Id;

        @JsonProperty( "user_id")
//        @NotNull
        private Long userId;

       @NotNull
       @Size(min = 3, max = 50, message = "Minimum 3 character title required and Max 50")
        @JsonProperty( "title")
        private  String title;

        @JsonProperty( "description")
        private  String description;

        @JsonProperty( "instock")
        private  Boolean instock;

        @Min(value = 0,message = "Minimum quantity 1")
        @Max(value = 500,message = "Maximun Quantity 500")
        @JsonProperty( "quantity")
        private  int quantity;

        @DecimalMin(value = "0.0", message = "Minimum Price 0 ")
        @DecimalMax(value = "50000.00",message = "Maximun Price 50000.00")
        @JsonProperty( "price")
        private  double price;

          private  Integer isdeleted;
//        @JsonProperty( "media_url")
//        private  List<String> media;

        @JsonProperty( "categories")
        private  List<String> categories;

        @JsonProperty( "tags")
        private  List<String> tags;

        @JsonProperty( "updated_at")
        private Instant updated_at;

}

