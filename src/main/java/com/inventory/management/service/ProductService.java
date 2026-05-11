package com.inventory.management.service;

import com.inventory.management.entity.*;
import com.inventory.management.repository.*;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {

	@Autowired
	private SaleItemRepository saleItemRepo;

	private final ProductRepository productRepo;

	private final CategoryRepository categoryRepo;

	private final ProcurementRepository procurementRepo;

	private final InventoryRepository inventoryRepo;

	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	public ProductService(ProductRepository productRepo, CategoryRepository categoryRepo,
			ProcurementRepository procurementRepo, InventoryService inventoryService,
			InventoryRepository inventoryRepo) {

		this.productRepo = productRepo;

		this.categoryRepo = categoryRepo;

		this.procurementRepo = procurementRepo;

		this.inventoryRepo = inventoryRepo;
	}

	// Product Creation

	public Product save(Product p) {

		log.info("Creating product : {}", p.getName());

		validateBarcode(p.getBarcode(), null);

		generateSku(p);

		applyDefaults(p);

		Product saved = productRepo.save(p);

		log.info("Product created successfully : {} | SKU : {}", saved.getName(), saved.getSku());

		return saved;
	}

	public List<Product> getAll() {

		log.info("Fetching all products");

		List<Product> products = productRepo.findAll();

		log.info("Total products fetched : {}", products.size());

		return products;
	}

	public Product getById(Long id) {

		log.info("Fetching product by ID : {}", id);

		return productRepo.findById(id).orElseThrow(() -> {

			log.error("Product not found : {}", id);

			return new RuntimeException("Product not found");
		});
	}

	public Product getByBarcode(String code) {

		log.info("Searching product by barcode : {}", code);

		Product product = productRepo.findByBarcode(code).orElse(null);

		if (product == null) {

			log.warn("No product found for barcode : {}", code);
		}

		else {

			log.info("Barcode matched product : {}", product.getName());
		}

		return product;
	}

	public List<Product> getActive() {

		log.info("Fetching active products");

		return productRepo.findByStatus("ACTIVE");
	}

	public List<Product> getPending() {

		log.info("Fetching pending products");

		return productRepo.findByStatus("PENDING");
	}

	public Map<String, Object> stats() {

		log.info("Generating product statistics");

		List<Product> all = productRepo.findAll();

		long active = all.stream().filter(p -> "ACTIVE".equals(p.getStatus())).count();

		long pending = all.stream().filter(p -> "PENDING".equals(p.getStatus())).count();

		long low = all.stream().filter(p -> (p.getStock() != null ? p.getStock()
				: 0) <= (p.getReorderLevel() != null ? p.getReorderLevel() : 10)).count();

		Map<String, Object> map = new HashMap<>();

		map.put("total", all.size());

		map.put("active", active);

		map.put("pending", pending);

		map.put("lowStock", low);

		log.info("Stats generated successfully");

		return map;
	}

	public List<Product> getByCategory(Long categoryId) {

		log.info("Fetching products by category : {}", categoryId);

		return productRepo.findByCategory_Id(categoryId);
	}

	public List<Product> search(String keyword) {

		log.info("Product search keyword : {}", keyword);

		if (keyword == null || keyword.isBlank()) {

			return productRepo.findAll();
		}

		return productRepo.findByNameContainingIgnoreCase(keyword);
	}

	// Product Update

	public Product update(Long id, Product body, Long categoryId) {

		log.info("Updating product ID : {}", id);

		Product p = getById(id);

		if (body.getBarcode() != null && !body.getBarcode().isBlank()) {

			validateBarcode(body.getBarcode(), id);

			p.setBarcode(body.getBarcode());
		}

		if (body.getName() != null)
			p.setName(body.getName());

		if (body.getUnit() != null)
			p.setUnit(body.getUnit());

		if (body.getPrice() != null)
			p.setPrice(body.getPrice());

		if (body.getMrp() != null)
			p.setMrp(body.getMrp());

		if (categoryId != null) {

			Category category = categoryRepo.findById(categoryId).orElseThrow();

			p.setCategory(category);
		}

		Product updated = productRepo.save(p);

		log.info("Product updated successfully : {}", updated.getName());

		return updated;
	}

	// Price Update

	public Product updatePrice(Long id, Double price, Double mrp) {

		log.info("Updating price for product ID : {}", id);

		Product p = getById(id);

		p.setPrice(BigDecimal.valueOf(price));

		p.setMrp(BigDecimal.valueOf(mrp));

		Product updated = productRepo.save(p);

		log.info("Price updated for : {}", updated.getName());

		return updated;
	}

	// Product Approval

	public Product approve(Long id, Double price, Double mrp) {

		log.info("Approving product ID : {}", id);

		Product p = getById(id);

		// GENERATE SKU

		if (p.getSku() == null || p.getSku().isBlank()) {

			generateSku(p);

			log.info("SKU generated : {}", p.getSku());
		}

		p.setPrice(BigDecimal.valueOf(price));

		p.setMrp(BigDecimal.valueOf(mrp));

		p.setStatus("ACTIVE");

		Product approved = productRepo.save(p);

		log.info("Product approved successfully : {}", approved.getName());

		return approved;
	}

	// Delete Product

	@Transactional
	public Product delete(Long id) {

		log.warn("Deleting product ID : {}", id);

		Product product = getById(id);

		inventoryRepo.deleteByProduct(product);

		procurementRepo.deleteByProduct_Id(id);

		saleItemRepo.deleteByProduct(product);

		productRepo.delete(product);

		log.warn("Product deleted successfully : {}", product.getName());

		return product;
	}

	// Barcode Validation

	private void validateBarcode(String barcode, Long currentId) {

		if (barcode == null || barcode.isBlank())
			return;

		Optional<Product> exists = productRepo.findByBarcode(barcode);

		if (exists.isPresent()) {

			if (currentId == null || !exists.get().getId().equals(currentId)) {

				log.error("Duplicate barcode detected : {}", barcode);

				throw new RuntimeException("Barcode already exists");
			}
		}
	}

	private void generateSku(Product p) {

		if (p.getSku() != null && !p.getSku().isBlank())
			return;

		String prefix = p.getName() == null ? "PRD"
				: p.getName().substring(0, Math.min(3, p.getName().length())).toUpperCase();

		String random = String.valueOf((int) (Math.random() * 9000) + 1000);

		p.setSku(prefix + "-" + random);

		log.info("Generated SKU : {}", p.getSku());
	}

	private void applyDefaults(Product p) {

		if (p.getStock() == null)
			p.setStock(0);

		if (p.getReorderLevel() == null)
			p.setReorderLevel(10);

		if (p.getSafetyStock() == null)
			p.setSafetyStock(5);

		if (p.getPrice() == null)
			p.setPrice(BigDecimal.ZERO);

		if (p.getMrp() == null)
			p.setMrp(BigDecimal.ZERO);

		if (p.getUnit() == null)
			p.setUnit("pcs");

		if (p.getStatus() == null)
			p.setStatus("ACTIVE");
	}

	public Product activate(Long id) {

		log.info("Activating product ID : {}", id);

		Product p = getById(id);

		p.setStatus("ACTIVE");

		Product updated = productRepo.save(p);

		log.info("Product activated : {}", updated.getName());

		return updated;
	}

	public Product deactivate(Long id) {

		log.warn("Deactivating product ID : {}", id);

		Product p = getById(id);

		p.setStatus("INACTIVE");

		Product updated = productRepo.save(p);

		log.warn("Product deactivated : {}", updated.getName());

		return updated;
	}
}