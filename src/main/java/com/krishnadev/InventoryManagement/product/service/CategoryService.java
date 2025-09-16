package com.krishnadev.InventoryManagement.product.service;

import com.krishnadev.InventoryManagement.product.dtos.CategoryDTO;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {


    public void addCategory(CategoryDTO category) {
            // check in redis, if category exists, not create it
         // else create new category
    }
}
