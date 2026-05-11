package com.inventory.management.controller;

import com.inventory.management.entity.*;
import com.inventory.management.service.MasterService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/masters")
@CrossOrigin("*")
public class MasterController {

	private final MasterService service;

	public MasterController(MasterService service) {
		this.service = service;
	}

	// CATEGORY

	@GetMapping("/categories")
	public List<Map<String, Object>> getCategories() {
		return service.getCategories();
	}

	@PostMapping("/categories")
	public Category addCategory(@RequestBody Category c) {
		return service.addCategory(c);
	}

	@PutMapping("/categories/{id}")
	public Category updateCategory(@PathVariable Long id, @RequestBody Category c) {
		return service.updateCategory(id, c);
	}

	@DeleteMapping("/categories/{id}")
	public String deleteCategory(@PathVariable Long id) {
		return service.deleteCategory(id);
	}

	// SUPPLIER

	@GetMapping("/suppliers")
	public List<Supplier> getSuppliers() {
		return service.getSuppliers();
	}

	@PostMapping("/suppliers")
	public Supplier addSupplier(@RequestBody Supplier s) {
		return service.addSupplier(s);
	}

	@PutMapping("/suppliers/{id}")
	public Supplier updateSupplier(@PathVariable Long id, @RequestBody Supplier s) {
		return service.updateSupplier(id, s);
	}

	@DeleteMapping("/suppliers/{id}")
	public String deleteSupplier(@PathVariable Long id) {
		return service.deleteSupplier(id);
	}

	// CUSTOMER

	@GetMapping("/customers")
	public List<Customer> getCustomers() {
		return service.getCustomers();
	}

	@PostMapping("/customers")
	public Customer addCustomer(@RequestBody Customer c) {
		return service.addCustomer(c);
	}

	@PutMapping("/customers/{id}")
	public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer c) {
		return service.updateCustomer(id, c);
	}

	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		service.deleteCustomer(id);
	}
}