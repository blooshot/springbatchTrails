package com.krishnadev.InventoryManagement.batch.reader;

import com.krishnadev.InventoryManagement.batch.Interface.TheReader;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class CustomReader implements TheReader {

    public CsvToBean createReader(String uploadedFile){

            return new CsvToBeanBuilder(setFile(uploadedFile))
                    .withType(ProductDTO.class)
                    .build();
           }

   public  Long getFileLineCount(String uploadedFile){
        long lineCount = 0;
        try {
            // This streams the file line by line without loading it all into memory
            lineCount = Files.lines(Paths.get(uploadedFile)).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Subtract 1 if your file has a header row
        return lineCount > 0 ? lineCount - 1 : 0;
    }

    public FileReader setFile(String uploadedFile){
        try {
            return new FileReader(uploadedFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}//EC
