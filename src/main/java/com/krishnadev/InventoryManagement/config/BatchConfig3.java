package com.krishnadev.InventoryManagement.config;

import com.krishnadev.InventoryManagement.config.Processor.ProductItemProcessor;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

//@Configuration
//@EnableBatchProcessing
public class BatchConfig3 {

//    @Autowired
    private DataSource dataSource;

    // --- Reader ---
    // Reads a single CSV file, using the file path passed as a job parameter.
//    @Bean
    public FlatFileItemReader<ProductDTO> reader() {
        FlatFileItemReader<ProductDTO> reader = new FlatFileItemReader<>();
        reader.setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("id", "firstName", "lastName", "email");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(ProductDTO.class);
            }});
        }});
        return reader;
    }

    // --- Processor ---
    // Validates each record. If validation fails, returns null.
    // The writer will handle the null values.
   /* @Bean
    public ItemProcessor<ProductDTO, ProductDTO> processor() {
        return ProductDTO -> {
            if (ProductDTO.getEmail() == null || com.krishnadev.InventoryManagement.product.dtos.ProductDTO.getEmail().isEmpty()) {
                System.out.println("Validation failed for: " + ProductDTO.getFirstName());
                return null; // This record will be ignored by the valid writer
            }
            return ProductDTO; // This record is valid
        };
    }*/
//    @Bean
    public ItemProcessor<ProductDTO, ProductEntity> processor() {
        return new ProductItemProcessor();
    }
    // --- Writer for Valid Records (to DB) ---
    // Writes valid ProductDTO objects to the 'ProductDTO' table in batches.
//    @Bean
    public JdbcBatchItemWriter<ProductDTO> databaseWriter() {
        JdbcBatchItemWriter<ProductDTO> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO ProductDTO (id, first_name, last_name, email) VALUES (:id, :firstName, :lastName, :email)");
        writer.setDataSource(dataSource);
        return writer;
    }

    // --- Writer for Invalid Records (to File) ---
    // Writes a simple string representation of invalid records to a CSV file.
  /*  @Bean
    public MultiResourceItemWriter<ProductDTO> fileWriter() {
        MultiResourceItemWriter<ProductDTO> writer = new MultiResourceItemWriter<>();
        writer.setResource(new FileSystemResource(new File("invalid_records.csv")));
        writer.setDelegate(item -> {
            // Write a custom string for each invalid item
            return item.getId() + "," + item.getFirstName() + "," + item.getLastName() + "," + item.getEmail() + "\n";
        });
        return writer;
    }*/

    // --- Composite Writer ---
    // This writer will handle both valid and invalid records.
    // We need custom logic to decide where to route the item.
    // Note: Spring Batch doesn't have a built-in splitter. This is a common pattern.
  /*  @Bean
    public CompositeItemWriter<ProductDTO> compositeItemWriter(JdbcBatchItemWriter<ProductDTO> databaseWriter, MultiResourceItemWriter<ProductDTO> fileWriter) {
        return new CompositeItemWriter<>() {
            @Override
            public void write(List<? extends ProductDTO> items) throws Exception {
                List<ProductDTO> validItems = new ArrayList<>();
                List<ProductDTO> invalidItems = new ArrayList<>();
                for (ProductDTO item : items) {
                    if (item.getEmail() == null || item.getEmail().isEmpty()) {
                        invalidItems.add(item);
                    } else {
                        validItems.add(item);
                    }
                }
                if (!validItems.isEmpty()) {
                    databaseWriter.write(validItems);
                }
                if (!invalidItems.isEmpty()) {
                    fileWriter.write(invalidItems);
                }
            }
        };
    }
*/
    // --- Step ---
    // Connects the reader, processor, and composite writer.
/*    @Bean
    public Step processFileStep() {
        return stepBuilderFactory.get("processFileStep")
                .<ProductDTO, ProductDTO>chunk(10) // Process 10 records at a time
                .reader(reader())
                .processor(processor())
                .writer(compositeItemWriter(databaseWriter(), fileWriter()))
                .faultTolerant()
                .skipPolicy(skipPolicy())
                .build();
    }*/
//    @Bean
/*    public Step processFileStep(ItemReader<ProductDTO> reader, ItemProcessor<ProductDTO, ProductEntity> processor, ItemWriter<Person> writer) {
        return stepBuilderFactory.get("processFileStep")
                .<ProductDTO, ProductEntity>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }*/



    // --- Job ---
    // The main job that runs the step.
    /*@Bean
    public Job processCsvJob(JobCompletionNotificationListener listener, Step processFileStep) {
        return jobBuilderFactory.get("processCsvJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(processFileStep)
                .end()
                .build();
    }*/
//    @Bean
/*    public Job processCsvJob(JobCompletionNotificationListener listener, Step processFileStep) {
        return jobBuilderFactory.get("processCsvJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(processFileStep)
                .end()
                .build();
    }*/
}