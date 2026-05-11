package com.inventory.management.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * ⚠️ DEPRECATED SERVICE
 *
 * This service was initially used for barcode mock lookup (demo purpose).
 * It is no longer actively used in the application.
 *
 * Current barcode scanning logic is handled via:
 * → ProductController / ProductService (DB-based lookup)
 *
 * NOTE:
 * Do NOT delete this file unless all references are removed from controllers.
 * Keeping this temporarily for fallback / testing.
 */
@Deprecated
@Service
public class BarcodeLookupService {

    public Map<String, Object> lookup(String barcode) {

        Map<String, Object> map = new HashMap<>();

        // DEMO MOCK DATA (LEGACY)
        if(barcode.equals("8901491101835")) {

            map.put("found", true);
            map.put("name", "Cream Biscuit");
            map.put("category", "Snacks");
            map.put("mrp", 30);
            map.put("unit", "pcs");

        } else if(barcode.equals("8906002481102")) {

            map.put("found", true);
            map.put("name", "Milk Packet");
            map.put("category", "Dairy");
            map.put("mrp", 28);
            map.put("unit", "pcs");

        } else {

            map.put("found", false);
        }

        return map;
    }
}