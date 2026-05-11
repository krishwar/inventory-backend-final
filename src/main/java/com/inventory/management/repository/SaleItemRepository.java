package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.management.entity.Product;
import com.inventory.management.entity.SaleItem;

import java.util.List;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findBySale_Id(Long saleId);

    List<SaleItem> findByProduct_Id(Long productId);
    
    
    void deleteByProduct(Product product);
}