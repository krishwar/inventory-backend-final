package com.inventory.management.controller;

import com.inventory.management.entity.Category;
import com.inventory.management.entity.Product;

import com.inventory.management.repository.CategoryRepository;
import com.inventory.management.repository.ProductRepository;

import com.inventory.management.service.ProductService;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

	private final ProductRepository productRepo;

	private final ProductService service;

	private final CategoryRepository categoryRepo;

	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	public ProductController(ProductService service, CategoryRepository categoryRepo, ProductRepository productRepo) {

		this.service = service;

		this.categoryRepo = categoryRepo;

		this.productRepo = productRepo;
	}

	@PostMapping
	public Product create(@RequestBody Map<String, Object> body) {

		log.info("API Create Product called");

		Product p = new Product();

		p.setName(body.get("name") != null ? body.get("name").toString() : "");

		p.setBarcode(body.get("barcode") != null ? body.get("barcode").toString() : null);

		p.setPrice(body.get("price") != null ? new BigDecimal(body.get("price").toString()) : BigDecimal.ZERO);

		p.setMrp(body.get("mrp") != null ? new BigDecimal(body.get("mrp").toString()) : BigDecimal.ZERO);

		p.setUnit(body.get("unit") != null ? body.get("unit").toString() : "pcs");

		p.setStatus("ACTIVE");

		if (body.get("categoryId") != null) {

			Long catId = Long.valueOf(body.get("categoryId").toString());

			Category c = categoryRepo.findById(catId).orElseThrow();

			p.setCategory(c);
		}

		Product saved = service.save(p);

		log.info("API Product created : {}", saved.getName());

		return saved;
	}

	@GetMapping
	public List<Product> getAll() {

		log.info("API Fetch All Products");

		return service.getAll();
	}

	@GetMapping("/{id}")
	public Product getById(@PathVariable Long id) {

		log.info("API Fetch Product by ID : {}", id);

		return service.getById(id);
	}

	@GetMapping("/search")
	public List<Product> search(@RequestParam String q) {

		log.info("API Product Search : {}", q);

		return service.search(q);
	}

	@GetMapping("/category/{id}")
	public List<Product> byCategory(@PathVariable Long id) {

		log.info("API Products By Category : {}", id);

		return service.getByCategory(id);
	}

	@PutMapping("/{id}")
	public Product update(@PathVariable Long id, @RequestBody Map<String, Object> body) {

		log.info("API Update Product : {}", id);

		Product p = new Product();

		if (body.get("name") != null)
			p.setName(body.get("name").toString());

		if (body.get("barcode") != null)
			p.setBarcode(body.get("barcode").toString());

		if (body.get("price") != null)
			p.setPrice(BigDecimal.valueOf(Double.parseDouble(body.get("price").toString())));

		if (body.get("mrp") != null)
			p.setMrp(BigDecimal.valueOf(Double.parseDouble(body.get("mrp").toString())));

		if (body.get("unit") != null)
			p.setUnit(body.get("unit").toString());

		Long categoryId = body.get("categoryId") == null ? null : Long.valueOf(body.get("categoryId").toString());

		return service.update(id, p, categoryId);
	}

	@PutMapping("/price/{id}")
	public Product updatePrice(@PathVariable Long id, @RequestBody Map<String, Object> body) {

		log.info("API Update Price : {}", id);

		Double price = Double.valueOf(body.get("price").toString());

		Double mrp = Double.valueOf(body.get("mrp").toString());

		return service.updatePrice(id, price, mrp);
	}

	@PutMapping("/activate/{id}")
	public Product activate(@PathVariable Long id) {

		log.info("API Activate Product : {}", id);

		return service.activate(id);
	}

	@PutMapping("/deactivate/{id}")
	public Product deactivate(@PathVariable Long id) {

		log.warn("API Deactivate Product : {}", id);

		return service.deactivate(id);
	}

	@PutMapping("/approve/{id}")
	public Product approve(@PathVariable Long id, @RequestBody Map<String, Object> body) {

		log.info("API Approve Product : {}", id);

		Double price = Double.valueOf(body.get("price").toString());

		Double mrp = Double.valueOf(body.get("mrp").toString());

		return service.approve(id, price, mrp);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id) {

		log.warn("API Delete Product : {}", id);

		service.delete(id);

		return "Product deleted successfully";
	}

	@GetMapping("/pending")
	public List<Product> pending() {

		log.info("API Fetch Pending Products");

		return service.getPending();
	}

	@GetMapping("/stats")
	public Map<String, Object> stats() {

		log.info("API Product Stats");

		return service.stats();
	}

	@GetMapping("/scan/{barcode}")
	public Map<String, Object> scan(@PathVariable String barcode) {

		log.info("API Barcode Scan : {}", barcode);

		Map<String, Object> res = new HashMap<>();

		Optional<Product> product = productRepo.findByBarcode(barcode.trim());

		if (product.isPresent()) {

			log.info("Barcode matched : {}", product.get().getName());

			res.put("exists", true);

			res.put("product", product.get());

		} else {

			log.warn("Barcode not found : {}", barcode);

			res.put("exists", false);
		}

		return res;
	}
}