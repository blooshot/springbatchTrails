package com.krishnadev.InventoryManagement.config.Processor;

import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class ValidationAndMapperProcessor implements ItemProcessor<ProductDTO, ProductEntity> {

    @Getter
    private final List<ProductDTO> invalidRecords = new ArrayList<>();
    public final Validator validator;

    public ValidationAndMapperProcessor(){
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }
    @Override
    public ProductEntity process(ProductDTO Item) throws Exception {
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(Item);
        ProductEntity productEntity =  new ProductEntity();
        if (!violations.isEmpty()) {
//            invalidRecords.add(Item);
            productEntity.setIsdeleted(true);
            return productEntity; // Return null to filter out this item from the main flows
        }
        log.debug("VPerfectItem -- ItemQTY: {} -- ItemPrice: {} ",Item.getQuantity(),Item.getPrice());
        // Map valid DTO to Entity

        productEntity.setTitle(Item.getTitle());
        productEntity.setDescription(Item.getDescription());
        productEntity.setQuantity(Item.getQuantity());
        productEntity.setPrice(Item.getPrice());
        productEntity.setInstock(Item.getInstock());
//        productEntity.setUserId(Item.getUserId());

        return productEntity;
    }

}//EC