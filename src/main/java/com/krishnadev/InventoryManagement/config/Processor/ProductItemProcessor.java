package com.krishnadev.InventoryManagement.config.Processor;

import com.krishnadev.InventoryManagement.config.ErrorRecordDTO;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ProductItemProcessor implements ItemProcessor<ProductDTO, ProductEntity> {

    private final Validator validator;
//    private final ThreadLocal<ErrorRecordDTO> errorRecordThreadLocal = new ThreadLocal<>();

    public ProductItemProcessor() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

//    public ErrorRecordDTO getErrorRecord() {
//        return errorRecordThreadLocal.get();
//    }

    @Override
    public ProductEntity process(ProductDTO item) throws Exception {
        // Clear any previous error
//        errorRecordThreadLocal.remove();

//        log.debug("Item: {}",item);
        // Perform business logic validation (e.g., check for negative price)
//        if (item.getPrice() < 0) {
//            errorRecordThreadLocal.set(new ErrorRecordDTO(item.toString(), "Price cannot be negative"));
//            return null; // Return null to indicate a skipped record
//        }

        // Perform standard Jakarta Validation checks
//        Errors errors = new BeanPropertyBindingResult(item, "item");
//        validator.validate(item)
//                .forEach(violation -> errors.rejectValue(violation.getPropertyPath().toString(), "", violation.getMessage()));

//        if (errors.hasErrors()) {
//            String errorMessages = errors.getAllErrors().stream()
//                    .map(e -> e.getDefaultMessage())
//                    .collect(Collectors.joining(", "));
//            errorRecordThreadLocal.set(new ErrorRecordDTO(item.toString(), errorMessages));
//            log.debug("ItemError: {}",errorMessages);
//            return null;
//        }

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(item);
        if (!violations.isEmpty()) {
            log.debug("ItemQTY: {} -- ItemPrice: {} -- ItemError: {}",item.getQuantity(),item.getPrice(),violations);
//            log.debug("",);
            // write Error Reader
//            writeInvalidRecord(item, violations);
            return null; // skip invalid record
        }
        log.debug("PerfectItem -- ItemQTY: {} -- ItemPrice: {} ",item.getQuantity(),item.getPrice());
        // Data transformation logic
        item.setTitle(item.getTitle().toUpperCase());
        item.setDescription(item.getDescription() != null ? item.getDescription().toLowerCase() : null);

        ProductEntity productEntity =  new ProductEntity();
        productEntity.setTitle(item.getTitle());
        productEntity.setDescription(item.getDescription());
        productEntity.setQuantity(item.getQuantity());
        productEntity.setPrice(item.getPrice());
        productEntity.setInstock(item.getInstock());
        productEntity.setUserId(item.getUserId());

        return productEntity;
    }
}
