package com.inventory.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@Table(name = "inventory_transactions")
public class InventoryTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;
	private Integer qty;
	private Integer balanceStock;
	private String remarks;

	private LocalDateTime createdAt;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnoreProperties({ "category" })
	private Product product;
}