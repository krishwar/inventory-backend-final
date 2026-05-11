package com.inventory.management.controller;

import com.inventory.management.dto.CheckoutRequest;
import com.inventory.management.dto.InvoiceResponse;

import com.inventory.management.service.BillingService;

import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin("*")
public class BillingController {

	private final BillingService service;

	private static final Logger log = LoggerFactory.getLogger(BillingController.class);

	public BillingController(BillingService service) {

		this.service = service;
	}

	@PostMapping("/checkout")
	public String checkout(@RequestBody CheckoutRequest req) {

		log.info("API Checkout request received");

		return service.checkout(req);
	}

	@GetMapping("/invoice/{id}")
	public InvoiceResponse invoice(@PathVariable Long id) {

		log.info("API Invoice request : {}", id);

		return service.getInvoice(id);
	}
}