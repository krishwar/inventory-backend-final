package com.inventory.management.service;

import com.inventory.management.entity.Product;
import com.inventory.management.entity.Procurement;
import com.inventory.management.entity.Sale;
import com.inventory.management.entity.SaleItem;

import com.inventory.management.repository.ProductRepository;
import com.inventory.management.repository.ProcurementRepository;
import com.inventory.management.repository.SaleItemRepository;
import com.inventory.management.repository.SaleRepository;

import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReportService {

	private final SaleRepository saleRepo;
	private final SaleItemRepository itemRepo;
	private final ProcurementRepository procurementRepo;
	private final ProductRepository productRepo;

	private static final Logger log = LoggerFactory.getLogger(ReportService.class);

	public ReportService(SaleRepository saleRepo, SaleItemRepository itemRepo, ProcurementRepository procurementRepo,
			ProductRepository productRepo) {

		this.saleRepo = saleRepo;
		this.itemRepo = itemRepo;
		this.procurementRepo = procurementRepo;
		this.productRepo = productRepo;
	}

	public Map<String, Object> dashboard() {

		log.info("Dashboard analytics loading started");

		Map<String, Object> map = new HashMap<>();

		List<Product> products = productRepo.findAll();

		List<SaleItem> items = itemRepo.findAll();

		log.info("Fetched products : {}", products.size());

		log.info("Fetched sale items : {}", items.size());

		// Total Products

		map.put("totalProducts", products.size());

		// Total Revenue

		double revenue = items.stream().mapToDouble(i -> i.getTotal() == null ? 0 : i.getTotal().doubleValue()).sum();

		map.put("totalRevenue", revenue);

		log.info("Calculated total revenue : {}", revenue);

		// Inventory Value

		double inventoryValue = products.stream()
				.mapToDouble(p -> (p.getPrice() == null ? 0 : p.getPrice().doubleValue())
						* (p.getStock() == null ? 0 : p.getStock()))
				.sum();

		map.put("inventoryValue", inventoryValue);

		log.info("Calculated inventory value : {}", inventoryValue);

		// Low Stock

		List<Map<String, Object>> lowStock = new ArrayList<>();

		List<Map<String, Object>> lowStockTable = new ArrayList<>();

		for (Product p : products) {

			int stock = p.getStock() == null ? 0 : p.getStock();

			int reorder = p.getReorderLevel() == null ? 10 : p.getReorderLevel();

			if (stock <= reorder) {

				log.warn("Low stock detected : {} | Remaining : {}", p.getName(), stock);

				Map<String, Object> m = new HashMap<>();

				m.put("name", p.getName());
				m.put("stock", stock);

				lowStock.add(m);

				Map<String, Object> t = new HashMap<>();

				t.put("name", p.getName());
				t.put("sku", p.getSku());
				t.put("stock", stock);
				t.put("reorderLevel", reorder);

				if (stock <= 2) {

					t.put("status", "CRITICAL");

					log.error("Critical stock level for : {}", p.getName());

				} else {

					t.put("status", "LOW");
				}

				lowStockTable.add(t);
			}
		}

		map.put("lowStock", lowStockTable);

		map.put("lowStockCount", lowStock.size());

		// TSP
		
		Map<String, Integer> productSales = new HashMap<>();

		for (SaleItem item : items) {

			String name = item.getProduct().getName();

			int qty = item.getQty() == null ? 0 : item.getQty();

			productSales.put(name, productSales.getOrDefault(name, 0) + qty);
		}

		List<Map<String, Object>> topProducts = productSales.entrySet().stream()
				.sorted((a, b) -> b.getValue() - a.getValue()).limit(5).map(e -> {

					Map<String, Object> m = new HashMap<>();

					m.put("name", e.getKey());

					m.put("value", e.getValue());

					return m;
				}).toList();

		map.put("topSellingData", topProducts);

		log.info("Top selling products calculated");
		
		//PIE CHART

		Map<String, Integer> categoryMap = new HashMap<>();

		for (Product p : products) {

			String cat = p.getCategory() != null ? p.getCategory().getName() : "Others";

			categoryMap.put(cat, categoryMap.getOrDefault(cat, 0) + (p.getStock() == null ? 0 : p.getStock()));
		}

		List<Map<String, Object>> categoryData = categoryMap.entrySet().stream().map(e -> {

			Map<String, Object> m = new HashMap<>();

			m.put("name", e.getKey());

			m.put("value", e.getValue());

			return m;
		}).toList();

		map.put("categoryData", categoryData);

		log.info("Category analytics prepared");

		// Revenue Trend

		List<Map<String, Object>> revenueTrend = new ArrayList<>();

		revenueTrend.add(createTrend("Mon", 1200));

		revenueTrend.add(createTrend("Tue", 1800));

		revenueTrend.add(createTrend("Wed", 900));

		revenueTrend.add(createTrend("Thu", 2200));

		revenueTrend.add(createTrend("Fri", revenue));

		map.put("revenueTrend", revenueTrend);

		// Inventory Category Value

		List<Map<String, Object>> inventoryCategoryValue = categoryMap.entrySet().stream().map(e -> {

			Map<String, Object> m = new HashMap<>();

			m.put("name", e.getKey());

			m.put("value", e.getValue());

			return m;
		}).toList();

		map.put("inventoryCategoryValue", inventoryCategoryValue);

		// Sales report

		List<Map<String, Object>> salesRows = new ArrayList<>();

		List<Sale> allSales = saleRepo.findAll();

		int unitsSold = 0;

		for (Sale s : allSales) {

			Map<String, Object> m = new HashMap<>();

			m.put("id", s.getId());

			m.put("billNo", s.getBillNo());

			m.put("paymentMode", s.getPaymentStatus());

			m.put("totalAmount", s.getGrandTotal());

			m.put("createdAt", s.getCreatedAt());

			salesRows.add(m);

			if (s.getItems() != null) {

				for (SaleItem item : s.getItems()) {

					unitsSold += item.getQty() == null ? 0 : item.getQty();
				}
			}
		}

		map.put("salesRows", salesRows);

		map.put("transactions", allSales.size());

		map.put("unitsSold", unitsSold);

		log.info("Sales report generated | Transactions : {}", allSales.size());

		// Purchase report

		List<Map<String, Object>> purchaseRows = new ArrayList<>();

		List<Procurement> allProcurements = procurementRepo.findAll();

		int unitsPurchased = 0;

		for (Procurement p : allProcurements) {

			Map<String, Object> m = new HashMap<>();

			m.put("productName", p.getProduct() != null ? p.getProduct().getName() : "-");

			m.put("supplier", p.getSupplier() != null ? p.getSupplier().getName() : "-");

			m.put("qty", p.getQty());

			m.put("costPrice", p.getCostPrice());

			m.put("totalCost", p.getTotalCost());

			m.put("date", p.getDate());

			purchaseRows.add(m);

			unitsPurchased += p.getQty() == null ? 0 : p.getQty();
		}

		map.put("purchaseRows", purchaseRows);

		map.put("unitsPurchased", unitsPurchased);

		map.put("totalPurchase", allProcurements.stream()
				.mapToDouble(p -> p.getTotalCost() == null ? 0 : p.getTotalCost().doubleValue()).sum());

		log.info("Purchase report generated | Records : {}", allProcurements.size());

		log.info("Dashboard analytics loaded successfully");

		return map;
	}

	private Map<String, Object> createTrend(String name, double value) {

		Map<String, Object> map = new HashMap<>();

		map.put("name", name);

		map.put("value", value);

		return map;
	}
}