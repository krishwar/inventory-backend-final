package com.inventory.management.controller;

import com.inventory.management.entity.Settings;
import com.inventory.management.service.SettingsService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin("*")
public class SettingsController {

	private final SettingsService service;

	public SettingsController(SettingsService service) {
		this.service = service;
	}

	@GetMapping
	public Settings get() {
		return service.getSettings();
	}

	@PostMapping
	public Settings save(@RequestBody Settings s) {
		return service.save(s);
	}
}