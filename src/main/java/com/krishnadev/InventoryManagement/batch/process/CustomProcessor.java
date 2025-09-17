package com.krishnadev.InventoryManagement.batch.process;

//import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishnadev.InventoryManagement.batch.writer.CustomWriter;
import com.krishnadev.InventoryManagement.config.BaseObjectTranslator;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.dtos.ProductErrorDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
@Slf4j
public class CustomProcessor {

    @Autowired
    CustomWriter writer;

    private final Validator validator;

    private final List<ProductErrorDTO> invalidRecordsList = new ArrayList<>();
    private final List<ProductDTO> validRecordsList = new ArrayList<>();

    @Setter
    private long totalRecordCount;
    @Setter
    private int chunkSize;

    @Autowired
    private BaseObjectTranslator objectMapper;

    CustomProcessor(){
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
//        objectMapper = new ModelMapper();
    }

    public void process(ProductDTO item) {

        validateProduct(item);

        // send chunk to Error writer and DB writer
        if(invalidRecordsList.size() >= chunkSize || validRecordsList.size() >= chunkSize){
            flushToWriter();
        }

        if(totalRecordCount == 1){
            flushToWriter();
            closeFile();
        }

        log.debug("TotalNumberOfRecord:{}",totalRecordCount);
        totalRecordCount--;
        log.debug("InvalidRecordSize:{}",invalidRecordsList.size());
        log.debug("validRecordSize:{}",validRecordsList.size());

    }

    private ProductDTO convertToDTO(Object item) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(item, ProductDTO.class);
    }

    public void setErroredRecordsPath(String errorFilePath) {
            writer.setErroredRecordsPath(errorFilePath);
//        log.debug("ProcessorPath: {}",errorFilePath);
    }

    private void flushToWriter(){
        if(! CollectionUtils.isEmpty(invalidRecordsList)) {
            writer.writeErroredRecords(invalidRecordsList);
            invalidRecordsList.clear();
        }

        if(! CollectionUtils.isEmpty(validRecordsList)) {
            writer.writeToDB(validRecordsList);
            validRecordsList.clear();
        }
    }

    private void validateProduct(ProductDTO item){
        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(item);

        if(!violations.isEmpty()){
            log.debug("Voliation Size: {}",violations.size());
            ProductErrorDTO productErrorDTO = objectMapper.map(item,ProductErrorDTO.class);
            productErrorDTO.setErrorMessage(getErrorMessage(violations));

            invalidRecordsList.add(productErrorDTO);
        }else {
            validRecordsList.add(item);
        }
    }

    public void closeFile(){
        try {
            writer.closeFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getErrorMessage(Set<ConstraintViolation<ProductDTO>> violations){
        List<String> errorMsgList =  violations.stream().map(x->
                "Error Message: " + x.getMessage() + ", FieldName: " + x.getPropertyPath() + ", Value: " + x.getInvalidValue() + "\n"
        ).collect(toList());

        return  String.join(" | ", errorMsgList);
    }


}//EC
