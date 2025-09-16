package com.krishnadev.InventoryManagement.product.controller;

import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job csvJob;

    private static final String FILE_UPLOAD_DIR = "src/main/resources/uploads/productCSVFile.csv";
    private static final String ERRORED_RECORDS_PATH = "src/main/resources/uploads/errored-records.csv";

   /* @PostMapping("/upload2/{fileName}")
    public ResponseEntity<String> uploadAndProcessCsv(@PathVariable(name = "fileName") String filename) {
//        if (file.isEmpty()) {
//            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
//        }

        try {
            // Ensure the upload and errored directories exist
//            Files.createDirectories(Path.of(FILE_UPLOAD_DIR));
//            Files.createDirectories(Path.of(ERRORED_RECORDS_PATH).getParent());
//
//            // Save the file to a temporary location
//            String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
//            File tempFile = new File(FILE_UPLOAD_DIR + filename);
//            file.transferTo(tempFile);

            // Set up job parameters with dynamic file paths
//            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
//            jobParametersBuilder.addString("fullPathFileName", FILE_UPLOAD_DIR);
//            jobParametersBuilder.addString("errorFilePath", ERRORED_RECORDS_PATH);
//            jobParametersBuilder.addLong("timestamp", System.currentTimeMillis());
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", FILE_UPLOAD_DIR)
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            // Launch the Spring Batch job
            JobExecution jobExecution = jobLauncher.run(job, jobParameters);


            // Return a success message with the job ID
            return ResponseEntity.ok("Batch job started with ID: " + jobExecution.getJobId());

        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to start batch job: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping("/upload3/{fileName}")
    public ResponseEntity<String> uploadCsv(@PathVariable("fileName") String filePath) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filePath",FILE_UPLOAD_DIR)
                .addString("errorFilePath",ERRORED_RECORDS_PATH)
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(csvJob, jobParameters);

        return ResponseEntity.accepted().body("CSV file is being processed.");
    }

    @PostMapping("/add")
    public ResponseEntity<String>   addProduct(@Valid @RequestBody List<ProductDTO> product){
//        if(errors.getErrorCount() > 1){
//            Map<String, String> validationErrors = new HashMap<>();
//            List<ObjectError> validationErrorList = errors.getAllErrors();
//
//            validationErrorList.forEach((error) -> {
//                String fieldName = ((FieldError) error).getField();
//                String validationMsg = error.getDefaultMessage();
//                validationErrors.put(fieldName, validationMsg);
//            });

//            return ResponseEntity.badRequest().body(errors.getAllErrors().toString());
//        }
//        log.debug("TheErrors1: "+ errors.getErrorCount());
//        productService.addProduct(product);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/update")
    public ResponseEntity<String>   updateProduct(@RequestBody List<ProductDTO> product){
        productService.update(product);
        return ResponseEntity.ok("success");
    }

    @PatchMapping("/update/{productID}")
    public ResponseEntity<String>   updateProductField(@RequestBody ProductDTO product){
        //first check redis, if not found in redis check DB
        // if found in redis, directly update in DB, else check DB and update id exists
        productService.updateProductField(product);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/upload")
    public ResponseEntity<String>   uploadProductCSV(@RequestBody List<ProductDTO> product){
//        productService.processCSV()
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("{productID}")
    public ResponseEntity<String>   deleteProduct(@PathVariable Long productID){
        productService.removeProduct(productID);
        return ResponseEntity.ok("success");
    }

    @GetMapping("{userID}/list")
    public ResponseEntity<String>   getAllProductOfUser(@PathVariable Long userID){
        //always get data from redis, if redis down, get paginated data from DB
        productService.getProductByUserID(userID);
        return ResponseEntity.ok("success");
    }


}//EC
