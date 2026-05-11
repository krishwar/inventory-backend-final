package com.inventory.management.controller;

import com.inventory.management.service.ReportService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin("*")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public Object dashboard() {
        return service.dashboard();
    }
}