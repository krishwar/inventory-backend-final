package com.inventory.management.dto;

import java.util.List;
import java.util.Map;

public class InvoiceResponse {

    private Map<String, Object> sale;

    private List<Map<String, Object>> items;

    public Map<String, Object> getSale() {
        return sale;
    }

    public void setSale(Map<String, Object> sale) {
        this.sale = sale;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }
}