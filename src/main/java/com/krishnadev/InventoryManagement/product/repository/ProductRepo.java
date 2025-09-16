package com.krishnadev.InventoryManagement.product.repository;


import com.krishnadev.InventoryManagement.product.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
}
