package com.inventory.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.management.entity.Settings;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}