package com.krishnadev.InventoryManagement.product.controller;

import com.krishnadev.InventoryManagement.product.dtos.CategoryDTO;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.service.CategoryService;
import com.krishnadev.InventoryManagement.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String>   createCategory(@RequestBody CategoryDTO category){
        categoryService.addCategory(category);
        return ResponseEntity.ok("success");
    }

}//EC
