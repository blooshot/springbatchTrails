package com.krishnadev.InventoryManagement.batch.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishnadev.InventoryManagement.config.BaseObjectTranslator;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.dtos.ProductErrorDTO;
import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import com.krishnadev.InventoryManagement.product.repository.ProductRepo;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class CustomWriter {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private BaseObjectTranslator mapper;

    private String errorFilePath;
    private Writer fileWriter;
    private StatefulBeanToCsv<ProductErrorDTO> csvWriter;

//    private CSVWriter csvWriter1;

    public void writeErroredRecords(List<ProductErrorDTO> item) {
        log.debug("ERROR-CustomWriter: {}",item.size());
        try {
            if (csvWriter == null) {
                initializeWriter();
            }
            csvWriter.write(item);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException| IOException e) {
            throw new RuntimeException(e);
        }
//        log.debug("ERRORPath-CustomWriter: {}",errorFilePath);
    }

    public void setErroredRecordsPath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }

    public void writeToDB(List<ProductDTO> item) {
        log.debug("DB-Adder-Custom writer:{}",item.size());
       List<ProductEntity> productEntityList =  item.stream().map(x->mapper.map(x,ProductEntity.class)).collect(toList());
//        item.stream().map(x->mapper.map(x,ProductEntity.class)).forEach(productRepository::save);
        productEntityList.stream().forEach(x->log.debug("DB Added:{}",x));
//        log.debug("DB-Adder-Custom writer:{}",);
        productRepository.saveAll(productEntityList);
    }

    private void initializeWriter() throws IOException {
        ColumnPositionMappingStrategy<ProductErrorDTO> mappingStrategy = new ColumnPositionMappingStrategy<>();
//        HeaderColumnNameMappingStrategy<ProductDTO> headerStrategy = new HeaderColumnNameMappingStrategyBuilder<ProductDTO>().build();
        // 2. Set the type of bean to be written
        mappingStrategy.setType(ProductErrorDTO.class);

        // 3. Define the specific fields to write and their order
        String[] columns = new String[]{"title","description","instock","quantity","price","errorMessage"};
        mappingStrategy.setColumnMapping(columns);

        this.fileWriter = new FileWriter(this.errorFilePath);
        this.csvWriter = new StatefulBeanToCsvBuilder<ProductErrorDTO>(fileWriter)
                .withMappingStrategy(mappingStrategy)
                .build();
    }

    public void closeFile() throws Exception {
        if (csvWriter != null) {
            fileWriter.close(); // Close the underlying file writer
        }
    }

}//EC
