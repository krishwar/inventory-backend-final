package com.inventory.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String billNo;

    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal grandTotal;

    private String customerName;
    private String customerMobile;

    private String paymentStatus;

    private String paymentMode;

    private BigDecimal cashPaid = BigDecimal.ZERO;

    private BigDecimal upiPaid = BigDecimal.ZERO;

    private BigDecimal cardPaid = BigDecimal.ZERO;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> items;
}