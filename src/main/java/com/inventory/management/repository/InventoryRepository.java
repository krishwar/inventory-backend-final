package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.management.entity.InventoryTransaction;
import com.inventory.management.entity.Product;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryTransaction, Long> {

	List<InventoryTransaction> findByProduct_IdOrderByIdDesc(Long productId);
	void deleteByProduct(Product product);
}