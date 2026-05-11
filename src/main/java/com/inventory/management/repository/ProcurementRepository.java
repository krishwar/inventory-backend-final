package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.management.entity.Procurement;

import java.time.LocalDate;
import java.util.List;
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {

	    List<Procurement> findBySupplier_Id(Long supplierId);

	    List<Procurement> findByPaymentStatus(String status);

	    List<Procurement> findByDateBetween(LocalDate start, LocalDate end);

	    List<Procurement> findByProduct_Id(Long id); 
	    
	    void deleteByProduct_Id(Long productId);
	}
