package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.management.entity.Product;

import java.util.*;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    Optional<Product> findBySku(String sku);

    Optional<Product> findByNameIgnoreCase(String name);
    
    Optional<Product> findByName(String name);

    List<Product> findByStatus(String status);

    List<Product> findByCategory_Id(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);
}