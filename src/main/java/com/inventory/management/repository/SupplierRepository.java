package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.management.entity.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findByStatus(String status);

    Optional<Supplier> findByName(String name);
    Optional<Supplier> findByNameIgnoreCase(String name);
}