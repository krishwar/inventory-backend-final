package com.inventory.management.controller;

import com.inventory.management.service.FinanceService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
@CrossOrigin("*")
public class FinanceController {

	private final FinanceService service;

	public FinanceController(FinanceService service) {
		this.service = service;
	}

	@GetMapping("/dashboard")
	public Object dashboard() {
		return service.dashboard();
	}
}