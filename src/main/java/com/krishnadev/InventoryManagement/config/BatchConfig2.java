package com.krishnadev.InventoryManagement.config;

import com.krishnadev.InventoryManagement.config.Processor.ProductItemProcessor;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

//@Slf4j
//@Configuration
//@EnableBatchProcessing
public class BatchConfig2 {

//    @Autowired
    private JobRepository jobRepository;

//    @Autowired
    private PlatformTransactionManager transactionManager;

//    @Autowired
    private DataSource dataSource;

//    @Value("${file.upload.dir:./uploads/}")
    private String uploadDir;

//    @Value("${file.errored.dir:./errored/}")
    private String erroredDir = "src/main/resources/uploads/";

    private static final String FILE_UPLOAD_DIR = "src/main/resources/uploads/productCSVFile.csv";
    private static final String ERRORED_RECORDS_PATH = "src/main/resources/uploads/errored-records.csv";

//    @Bean
//    public FlatFileItemReader<ProductDTO> reader(@Value("#{jobParameters['fullPathFileName']}") String filePath) {
//        return new FlatFileItemReaderBuilder<ProductDTO>()
//                .name("productReader")
//                .resource(new FileSystemResource(filePath))
//                .linesToSkip(1) // Assuming header row
//                .delimited()
//                .names("title", "description","instock","quantity", "price")
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<ProductDTO>() {{
//                    setTargetType(ProductDTO.class);
//                }})
//                .build();
//    }

//    @Bean
//    public ProductItemProcessor processor() {
//        return new ProductItemProcessor();
//    }

//    @Bean
//    public ItemWriter<ProductEntity> writer(DataSource dataSource) {
//        return items -> {
//            String sql = "INSERT INTO product (title, description,instock,quantity, price) VALUES (?, ?, ?, ?)";
//            log.debug("DB Item: {}",items);
//
////            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
////            jdbcTemplate.batchUpdate(sql, items, 100, (ps, item) -> {
////                ps.setString(1, item.getTitle());
////                ps.setString(2, item.getName());
////                ps.setDouble(3, item.getPrice());
////                ps.setString(4, item.getDescription());
////            });
//        };
//    }

//    @Bean
  /*  public FlatFileItemWriter<ErrorRecordDTO> errorWriter() {
        log.debug("ErrorWriter Tak aya to Sahi: {}",ERRORED_RECORDS_PATH);
        return new FlatFileItemWriterBuilder<ErrorRecordDTO>()
                .name("errorWriter")
                .resource(new FileSystemResource(ERRORED_RECORDS_PATH))
                .delimited()
                .names("line", "reason")
                .shouldDeleteIfExists(true)
                .build();
    }*/

//    @Bean
  /*  public Step step1(FlatFileItemReader<ProductDTO> reader, ProductItemProcessor processor, ItemWriter<ProductEntity> writer) {
        return new StepBuilder("step1", jobRepository)
                .<ProductDTO, ProductEntity>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
//                .skipPolicy(new ValidationSkipPolicy(errorWriter())) // Custom skip policy
                .build();
    }*/

//    @Bean
/*    public Job importProductsJob(JobCompletionListener listener, Step step1) {
        return new JobBuilder("importProductsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }*/
}
