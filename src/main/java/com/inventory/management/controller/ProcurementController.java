package com.inventory.management.controller;

import com.inventory.management.entity.Procurement;

import com.inventory.management.service.ProcurementService;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/procurement")
@CrossOrigin("*")
public class ProcurementController {

	private final ProcurementService service;

	private static final Logger log = LoggerFactory.getLogger(ProcurementController.class);

	public ProcurementController(ProcurementService service) {

		this.service = service;
	}

	@GetMapping
	public List<Procurement> getAll() {

		log.info("API Fetch Procurement History");

		return service.getAll();
	}

	@PostMapping("/import-excel")
	public Map<String, Object> importExcel(@RequestParam("file") MultipartFile file) {

		log.info("API Excel Import Started");

		return service.importExcel(file);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id) {

		log.warn("API Delete Procurement : {}", id);

		service.delete(id);

		return "Deleted";
	}

	@PutMapping("/payment/{id}")
	public Procurement updatePayment(@PathVariable Long id, @RequestParam Double amount) {

		log.info("API Procurement Payment Update | ID : {} | Amount : {}", id, amount);

		return service.updatePayment(id, amount);
	}
	
	@PostMapping
	public Procurement save(@RequestBody Map<String, Object> body) {

	    Long productId = Long.valueOf(body.get("productId").toString());

	    Long supplierId = Long.valueOf(body.get("supplierId").toString());

	    Integer qty = Integer.valueOf(body.get("qty").toString());

	    Double costPrice = Double.valueOf(body.get("costPrice").toString());

	    return service.save(productId, supplierId, qty, costPrice);
	}
}