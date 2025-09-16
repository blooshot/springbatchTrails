package com.krishnadev.InventoryManagement.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishnadev.InventoryManagement.product.dtos.ProductDTO;
import com.krishnadev.InventoryManagement.product.repository.CategoryRepo;
import com.krishnadev.InventoryManagement.product.repository.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ProductService {

    private  ProductRepo productRepo;

    private CategoryRepo categoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    public List<ProductDTO> getProductByUserID(Long userID){
        List<ProductDTO> productDTOList = null;
        // get from redis, if not found
        // get from database
        return productDTOList;
    }

    public void addProduct(ProductDTO product) {
            // create new product

            // return boolean
    }

    public void update(List<ProductDTO> product) {
        // check if product exists, then update or directly update and throw error if not found
        // return boolean
    }

    public void updateProductField(ProductDTO product) {
        // check in DB if product exists, update the field or throw error
        // return boolean
    }

    public void removeProduct(Long productID) {
        // mark product isDeleted=true, or throw error
        // return boolean
    }
}//EC

