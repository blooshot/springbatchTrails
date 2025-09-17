package com.krishnadev.InventoryManagement.product.controller;

import com.krishnadev.InventoryManagement.batch.CsvExecutionInitializer;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;


@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CsvExecutionInitializer csvOrca;

    private static final String FILE_UPLOAD_DIR = "src/main/resources/uploads/MOCK_DATA.csv";
    private static final String ERRORED_RECORDS_PATH = "src/main/resources/uploads/errored-records.csv";

    @PostMapping("/uploadFile")
    public ResponseEntity<String>   uploadFile(){
        try {
            csvOrca.processCSV(FILE_UPLOAD_DIR,ERRORED_RECORDS_PATH);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok("success");
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
