package com.krishnadev.InventoryManagement.batch;

import com.krishnadev.InventoryManagement.batch.process.CustomProcessor;
import com.krishnadev.InventoryManagement.batch.reader.CustomReader;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

@Slf4j
@Component
public class CsvExecutionInitializer {

    @Autowired
    private CustomProcessor processor;

    @Autowired
    private CustomReader reader;


    public void processCSV(String uploadFilePath, String ErrorFilePath) throws FileNotFoundException {

        Stream<ProductDTO> dtoStream = reader.createReader(uploadFilePath).stream();
        processor.setErroredRecordsPath(ErrorFilePath);
        processor.setTotalRecordCount(reader.getFileLineCount(uploadFilePath));
        processor.setChunkSize(100);

            try (dtoStream) {
                    dtoStream.forEach(processor::process);
            }finally {

            }

    }
}//EC
