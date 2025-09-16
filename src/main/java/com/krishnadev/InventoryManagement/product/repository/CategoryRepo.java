package com.krishnadev.InventoryManagement.product.repository;

import com.krishnadev.InventoryManagement.product.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {

}
