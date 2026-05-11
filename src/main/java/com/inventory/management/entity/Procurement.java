package com.inventory.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "procurements")
public class Procurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer qty;

    private BigDecimal costPrice;
    private BigDecimal totalCost;

    private BigDecimal paidAmount;
    private BigDecimal dueAmount;

    private String paymentStatus;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
}