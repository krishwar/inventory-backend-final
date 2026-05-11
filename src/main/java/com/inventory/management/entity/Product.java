package com.inventory.management.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String barcode;

    private String sku;

    private Integer stock = 0;

    private Integer reorderLevel = 10;

    private Integer safetyStock = 5;

    private BigDecimal price = BigDecimal.ZERO;

    private BigDecimal mrp = BigDecimal.ZERO;

    private String unit = "pcs";

    private String status = "ACTIVE";

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("products")
    private Category category;
}