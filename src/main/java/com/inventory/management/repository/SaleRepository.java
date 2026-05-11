package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.management.entity.Sale;

import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByBillNo(String billNo);
}