package com.inventory.management.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "settings")
public class Settings {

    @Id
    private Long id = 1L;

    private Integer safetyStock;
    private Integer minimumThreshold;
    private Integer reorderLevel;

    private String storeName;
    private String currencySymbol;
}