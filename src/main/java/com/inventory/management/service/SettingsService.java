package com.inventory.management.service;

import com.inventory.management.entity.Settings;
import com.inventory.management.repository.SettingsRepository;

import org.springframework.stereotype.Service;

@Service
public class SettingsService {

    private final SettingsRepository repo;

    public SettingsService(SettingsRepository repo) {
        this.repo = repo;
    }

    public Settings getSettings() {
        return repo.findById(1L).orElseGet(() -> {
            Settings s = new Settings();
            s.setSafetyStock(5);
            s.setReorderLevel(10);
            s.setStoreName("StockFlow");
            s.setCurrencySymbol("₹");
            return repo.save(s);
        });
    }

    public Settings save(Settings input) {
        input.setId(1L);
        return repo.save(input);
    }
}