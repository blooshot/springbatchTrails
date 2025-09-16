package com.krishnadev.InventoryManagement.config;

import com.krishnadev.InventoryManagement.product.batch.TextItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
public class BatchConfig {

//    @Bean
    protected FlatFileItemReader<String> reader(){
        return new FlatFileItemReaderBuilder<String>()
                .resource(new ClassPathResource("data_file.csv"))
                .name("csv-reader")
                .lineMapper((line,lineNumber)-> line)
                .build();
    }

//    @Bean
    protected FlatFileItemWriter<String> writer(){
        String fileLocation = "src/main/resources/masked-data.csv";
        return new FlatFileItemWriterBuilder<String>()
                .name("csv-writer")
                .resource(new FileSystemResource(fileLocation))
                .lineAggregator((line)-> line)
                .build();
    }

//    @Bean
    protected Step maskingStep(JobRepository jobRepo, PlatformTransactionManager manager,
                               FlatFileItemReader<String> reader, TextItemProcessor processor, FlatFileItemWriter<String>writer){

        return new StepBuilder("mask-step", jobRepo)
                .<String,String>chunk(2, manager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//    @Bean
    protected Job maskingJob(JobRepository jobRepo, Step maskingStep){
        return  new JobBuilder("masking-job",jobRepo)
                .start(maskingStep)
                .build();
    }

}//EC
