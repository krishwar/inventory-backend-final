package com.inventory.management.service;

import com.inventory.management.entity.*;
import com.inventory.management.repository.*;

import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MasterService {

	private final CategoryRepository categoryRepo;
	private final SupplierRepository supplierRepo;
	private final CustomerRepository customerRepo;
	private final SaleItemRepository itemRepo;

	public MasterService(CategoryRepository categoryRepo, SupplierRepository supplierRepo,
			CustomerRepository customerRepo, ProductRepository productRepo, SaleItemRepository itemRepo) {
		this.categoryRepo = categoryRepo;
		this.supplierRepo = supplierRepo;
		this.customerRepo = customerRepo;
		this.itemRepo = itemRepo;
	}

	private static final Logger log = LoggerFactory.getLogger(MasterService.class);

	// CATEGORY

	public List<Map<String, Object>> getCategories() {

		log.info("Fetching active categories");

		List<Category> categories = categoryRepo.findByStatus("ACTIVE");

		List<SaleItem> soldItems = itemRepo.findAll();

		Set<Long> billedCategoryIds = new HashSet<>();

		for (SaleItem item : soldItems) {

			Product product = item.getProduct();

			if (product != null && product.getCategory() != null) {
				billedCategoryIds.add(product.getCategory().getId());
			}
		}

		List<Map<String, Object>> result = new ArrayList<>();

		for (Category c : categories) {

			boolean billed = billedCategoryIds.contains(c.getId());

			Map<String, Object> map = new HashMap<>();

			map.put("id", c.getId());
			map.put("name", c.getName());
			map.put("status", c.getStatus());

			map.put("canDelete", !billed);

			result.add(map);
		}

		log.info("Total active categories fetched : {}", result.size());

		return result;
	}

	public Category addCategory(Category c) {

		log.info("Creating category : {}", c.getName());

		c.setStatus("ACTIVE");

		Category saved = categoryRepo.save(c);

		log.info("Category created successfully : {}", saved.getName());

		return saved;
	}

	public Category updateCategory(Long id, Category body) {

		log.info("Updating category ID : {}", id);

		Category c = categoryRepo.findById(id).orElseThrow(() -> {

			log.error("Category not found : {}", id);

			return new RuntimeException("Category not found");
		});

		c.setName(body.getName());

		Category updated = categoryRepo.save(c);

		log.info("Category updated successfully : {}", updated.getName());

		return updated;
	}

	public String deleteCategory(Long id) {

		log.info("Delete requested for category ID : {}", id);

		List<SaleItem> soldItems = itemRepo.findAll();

		boolean billed = soldItems.stream().anyMatch(item -> item.getProduct() != null
				&& item.getProduct().getCategory() != null && item.getProduct().getCategory().getId().equals(id));

		if (billed) {

			log.warn("Attempted delete on billed category ID : {}", id);

			return "Category already used in billing. Cannot delete.";
		}

		Category c = categoryRepo.findById(id).orElseThrow(() -> {

			log.error("Category not found for delete : {}", id);

			return new RuntimeException("Category not found");
		});

		log.info("Deleting category : {}", c.getName());

		categoryRepo.delete(c);

		log.info("Category deleted successfully : {}", c.getName());

		return "Deleted successfully";
	}

	// ================= SUPPLIER =================

	public List<Supplier> getSuppliers() {

		log.info("Fetching suppliers");

		List<Supplier> suppliers = supplierRepo.findByStatus("ACTIVE");

		log.info("Total suppliers fetched : {}", suppliers.size());

		return suppliers;
	}

	public Supplier addSupplier(Supplier s) {

		log.info("Adding supplier : {}", s.getName());

		s.setStatus("ACTIVE");

		Supplier saved = supplierRepo.save(s);

		log.info("Supplier added successfully : {}", saved.getName());

		return saved;
	}

	public Supplier updateSupplier(Long id, Supplier body) {

		log.info("Updating supplier ID : {}", id);

		Supplier s = supplierRepo.findById(id).orElseThrow(() -> {

			log.error("Supplier not found : {}", id);

			return new RuntimeException("Supplier not found");
		});

		s.setName(body.getName());

		Supplier updated = supplierRepo.save(s);

		log.info("Supplier updated successfully : {}", updated.getName());

		return updated;
	}

	public String deleteSupplier(Long id) {

		log.info("Deactivating supplier ID : {}", id);

		Supplier s = supplierRepo.findById(id).orElseThrow(() -> {

			log.error("Supplier not found for delete : {}", id);

			return new RuntimeException("Supplier not found");
		});

		s.setStatus("INACTIVE");

		supplierRepo.save(s);

		log.info("Supplier deactivated successfully : {}", s.getName());

		return "Supplier deactivated";
	}

	// ================= CUSTOMER =================

	public List<Customer> getCustomers() {

		log.info("Fetching customers");

		List<Customer> customers = customerRepo.findByStatus("ACTIVE");

		log.info("Total customers fetched : {}", customers.size());

		return customers;
	}

	public Customer addCustomer(Customer c) {

		log.info("Adding customer : {}", c.getName());

		c.setStatus("ACTIVE");

		Customer saved = customerRepo.save(c);

		log.info("Customer added successfully : {}", saved.getName());

		return saved;
	}

	public Customer updateCustomer(Long id, Customer body) {

		log.info("Updating customer ID : {}", id);

		Customer c = customerRepo.findById(id).orElseThrow(() -> {

			log.error("Customer not found : {}", id);

			return new RuntimeException("Customer not found");
		});

		c.setName(body.getName());

		Customer updated = customerRepo.save(c);

		log.info("Customer updated successfully : {}", updated.getName());

		return updated;
	}

	public void deleteCustomer(Long id) {

		log.info("Deleting customer ID : {}", id);

		Customer c = customerRepo.findById(id).orElseThrow(() -> {

			log.error("Customer not found for delete : {}", id);

			return new RuntimeException("Customer not found");
		});

		customerRepo.delete(c);

		log.info("Customer deleted successfully : {}", c.getName());
	}
}