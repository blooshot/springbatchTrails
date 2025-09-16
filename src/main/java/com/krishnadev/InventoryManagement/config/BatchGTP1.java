package com.krishnadev.InventoryManagement.config;

import com.krishnadev.InventoryManagement.config.Processor.ProductItemProcessor;
import com.krishnadev.InventoryManagement.config.Processor.ValidationAndMapperProcessor;
import com.krishnadev.InventoryManagement.config.Writer.DatabaseConfigWriter;
import com.krishnadev.InventoryManagement.config.Writer.InvalidRecordWriter;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import com.krishnadev.InventoryManagement.product.repository.ProductRepo;
import jakarta.validation.ConstraintViolation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.JobBuilderHelper;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
@EnableBatchProcessing
@EnableAsync


public class BatchGTP1 {

//    @Autowired
//    private GenericValidator genericValidator;

//    @Autowired
//    JobRepository jobRepository;

//    @Autowired
//    PlatformTransactionManager transactionManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductRepo recordRepository;

    @Bean
    public Job csvJob(Step step1, JobCompletionListener listener,JobRepository jobRepository) {
//        return jobBuilderFactory("csvJob",jobRepository).
//incrementer(new RunIdIncrementer())
//                .flow(step1)
//                .end()
//                .build();
        return new JobBuilder("csvJob",jobRepository)
                .incrementer(new RunIdIncrementer())
//                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

//    @Bean
//    public Step step1(ItemReader<ProductDTO> reader, ItemProcessor<ProductDTO, ProductEntity> processor,
//                      ItemWriter<ProductEntity> writer,   PlatformTransactionManager transactionManager, JobRepository jobRepository) {
////        return stepBuilderFactory("step1",jobRepository)
////                .<Record, Record>chunk(100)
////                .reader(reader)
////                .processor(processor)
////                .writer(writer)
////                .build();
//        return new StepBuilder("step1", jobRepository)
//                .<ProductDTO, ProductEntity>chunk(100, transactionManager)
//                .reader(reader)
//                .processor(processor)
////                .writer(writer)
//                .writer(compositeItemWriter())
////                .faultTolerant()
////                .skipPolicy()
//                .build();
//    }

/*    @Bean
    public CompositeItemWriter<T> compositeItemWriter() {
   *//*     return new CompositeItemWriter<ProductDTO>() {
            @Override
            public void write(List<? extends ProductDTO> items) throws Exception {
                Chunk<ProductEntity> validItems = new Chunk<>();
                Chunk<ProductDTO> invalidItems = new Chunk<>();
                for (ProductDTO item : items) {
                    Set<ConstraintViolation<ProductDTO>> violations = genericValidator.validator.validate(item);
                    if (!violations.isEmpty()) {
//                        writeInvalidRecord(item, violations);
                        invalidItems.add(item);
//                        return null; // skip invalid record
                    }else {
                        ProductEntity productEntity =  new ProductEntity();
                        productEntity.setTitle(item.getTitle());
                        productEntity.setDescription(item.getDescription());
                        productEntity.setQuantity(item.getQuantity());
                        productEntity.setPrice(item.getPrice());
                        productEntity.setInstock(item.getInstock());
                        productEntity.setUserId(item.getUserId());
                        validItems.add(productEntity);
                    }
                    if (!validItems.isEmpty()) {
                        databaseWriter.write(validItems);
                    }
                    if (!invalidItems.isEmpty()) {
                        fileWriter.write(invalidItems);
                    }
                }
            };
        };*//*

        CompositeItemWriter<ProductDTO> compositeWriter = new CompositeItemWriter<>();
        compositeWriter.setDelegates(Arrays.asList(fileWriter(errorFilePath), databaseWriter()));
        return compositeWriter;
    }*/

@Bean
public Step processData(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
//        JdbcBatchItemWriter<ProductEntity> dbWriter,
//        ItemWriter<ProductEntity> InvalidWriter,
        ItemReader<ProductDTO> reader
       ) {

    // The CompositeItemWriter will delegate to the database writer
    // and your custom CSV writer.
    CompositeItemWriter<ProductEntity> compositeWriter = new CompositeItemWriter<>();
    compositeWriter.setDelegates(List.of(InvalidWriter(),DBWriter()));
//    compositeWriter.setDelegates(List.of(InvalidWriter,DBWriter())); // with Beans

    return new StepBuilder("processDataStep", jobRepository)
            .<ProductDTO, ProductEntity>chunk(10, transactionManager) // Batch size of 10
            .reader(reader)
            .processor(processor())
            .writer(compositeWriter)
            .build();
}
    @Bean
    @StepScope
    public FlatFileItemReader<ProductDTO> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<ProductDTO> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public LineMapper<ProductDTO> lineMapper() {
        DefaultLineMapper<ProductDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("title", "description","instock","quantity", "price");
        tokenizer.setStrict(false);

        BeanWrapperFieldSetMapper<ProductDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProductDTO.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

//    @Bean
//    public ItemProcessor<ProductDTO, ProductEntity> processor() {
//       /* return record -> {
//            List<String> errors = new ArrayList<>();
//            if (record.getAge() < 18) {
//                errors.add("Age must be >= 18");
//            }
//            if (!record.getEmail().contains("@")) {
//                errors.add("Invalid email");
//            }
//            if (!errors.isEmpty()) {
//                writeInvalidRecord(record, errors);
//                return null; // skip invalid record
//            }
//            return record;
//        };*/
//        return new ProductItemProcessor();
//    }

//    @Order(0)
//    @Bean
    public ItemProcessor<ProductDTO, ProductEntity>  processor() {
        return new ValidationAndMapperProcessor();
    }

//    @Order(2)
//    @Bean
    public ItemWriter<ProductEntity>  InvalidWriter(@Value("#{jobParameters['filePath']}") String ErrorfilePath) {
        return new InvalidRecordWriter(ErrorfilePath);
    }

    public ItemWriter<ProductEntity> DBWriter() {
        return new DatabaseConfigWriter();
    }

//    @Async
//    public void writeInvalidRecord(ProductDTO record,  Set<ConstraintViolation<ProductDTO>> errors) {
//       /* try {
//            File file = new File("invalid-records.csv");
//            boolean fileExists = file.exists();
//
//            FileWriter fw = new FileWriter(file, true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            if (!fileExists) {
//                bw.write("name,email,age,errors");
//                bw.newLine();
//            }
//            bw.write(record.getName() + "," + record.getEmail() + "," + record.getAge() + "," + String.join(";", errors));
//            bw.newLine();
//            bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }*/
//        System.out.println("Invalid Record: "+ record);
//        System.out.println("Invalid Record Errors: "+ errors);
//    }

//    @Bean
//    public ItemWriter<ProductEntity> writer() {
//        return records -> System.out.println("BufferedWriter: "+ records);
//    }

//    @Bean
//    public JdbcBatchItemWriter<ProductEntity> databaseWriter() {
//        JdbcBatchItemWriter<ProductEntity> writer = new JdbcBatchItemWriter<>();
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//        writer.setSql("INSERT INTO products (title, description,instock,quantity, price) VALUES (:title,:description,:instock,:quantity,:price)");
//        writer.setDataSource(dataSource);
//        return writer;
//    }

    // --- Writer for Invalid Records (to File) ---
    // Writes a simple string representation of invalid records to a CSV file.
//    @Bean
//    public MultiResourceItemWriter<ProductDTO> fileWriter(String filePath) {
//        MultiResourceItemWriter<ProductDTO> writer = new MultiResourceItemWriter<>();
//        writer.setResource(new FileSystemResource(filePath));
//        writer.setDelegate(item -> {
//            // Write a custom string for each invalid item
////            return item.getId() + "," + item.getFirstName() + "," + item.getLastName() + "," + item.getEmail() + "\n";
//            return item.toString() + "\n";
//        });
//        return writer;
//    }
}
