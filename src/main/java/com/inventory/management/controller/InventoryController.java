package com.inventory.management.controller;

import com.inventory.management.entity.InventoryTransaction;
import com.inventory.management.entity.Product;
import com.inventory.management.repository.InventoryRepository;
import com.inventory.management.repository.ProductRepository;
import com.inventory.management.service.InventoryService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin("*")
public class InventoryController {

	private final InventoryService inventoryService;
	private final ProductRepository productRepo;
	private final InventoryRepository inventoryRepo;

	public InventoryController(InventoryService inventoryService, ProductRepository productRepo,
			InventoryRepository inventoryRepo) {
		this.inventoryService = inventoryService;
		this.productRepo = productRepo;
		this.inventoryRepo = inventoryRepo;
	}

	@GetMapping
	public List<Product> all() {
		return productRepo.findAll();
	}

	@GetMapping("/history")
	public List<InventoryTransaction> history() {
		return inventoryRepo.findAll();
	}

	@PostMapping("/damage")
	public String damage(@RequestParam Long productId, @RequestParam Integer qty) {

		Product p = productRepo.findById(productId).orElseThrow();

		inventoryService.decreaseStock(p, qty, "DAMAGE", "Damaged");

		return "Stock updated";
	}

	@PostMapping("/return")
	public String returns(@RequestParam Long productId, @RequestParam Integer qty) {

		Product p = productRepo.findById(productId).orElseThrow();

		inventoryService.increaseStock(p, qty, "RETURN", "Returned");

		return "Stock updated";
	}

	@PostMapping("/adjust")
	public String adjust(@RequestParam Long productId, @RequestParam Integer qty, @RequestParam String mode,
			@RequestParam(required = false) String remarks) {

		Product p = productRepo.findById(productId).orElseThrow();

		if ("IN".equalsIgnoreCase(mode)) {

			inventoryService.increaseStock(p, qty, "ADJUST_IN", remarks);

		} else {

			inventoryService.decreaseStock(p, qty, "ADJUST_OUT", remarks);
		}

		return "Adjusted";
	}
}