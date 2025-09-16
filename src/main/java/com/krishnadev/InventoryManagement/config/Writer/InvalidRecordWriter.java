package com.krishnadev.InventoryManagement.config.Writer;

import com.krishnadev.InventoryManagement.config.Processor.ValidationAndMapperProcessor;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class InvalidRecordWriter implements ItemWriter<ProductEntity> {

    @Autowired
    private  ValidationAndMapperProcessor processor;

    private String errorfilePath;

    public InvalidRecordWriter(String errorfilePath) {
        this.errorfilePath = errorfilePath;
    }


    @Override
    public void write(Chunk<? extends ProductEntity> ItemChunk) throws Exception {
        // Get the invalid records from the processor
        List<ProductDTO> invalidRecords = null;
        if(null != processor){
            invalidRecords = processor.getInvalidRecords();
        }

        // Write the invalid records to your CSV file
        // You would use a FlatFileItemWriter or similar here
        // Note: This is an oversimplified example. The actual implementation
        // would involve configuring a FlatFileItemWriter to write to a file.
        // For simplicity, we just print them here.
        log.debug("Writing invalid records to: {}", errorfilePath);
        if(null != invalidRecords && !invalidRecords.isEmpty()){
            invalidRecords.forEach(x->log.debug("ValidationFailed: {}",x));

            // Clear the list after writing to avoid duplicates in the next chunk
        invalidRecords.clear();
        }
        log.debug("ChunkSize: {}",ItemChunk.getItems().size());
        ItemChunk.getItems().stream().map(x-> (ProductEntity) x)
                .forEach(x->{
                    if(x.getIsdeleted()){

                    }
                    log.debug("Deletable: {}", x.getIsdeleted());
                    log.debug("Chunked Items: {}",x.toString());
                });
//        log.debug("Chunked Items: {}",ItemChunk);
//        log.debug("Chunked Errored Items: {}",ItemChunk.getErrors());
    }

//    @Override
//    public void write(Chunk<? extends ProductEntity> chunk) throws Exception {
//
//    }
}