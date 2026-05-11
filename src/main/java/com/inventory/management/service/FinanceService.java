package com.inventory.management.service;

import com.inventory.management.entity.Procurement;
import com.inventory.management.entity.Sale;

import com.inventory.management.repository.ProcurementRepository;
import com.inventory.management.repository.SaleRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FinanceService {

	private final SaleRepository saleRepo;

	private final ProcurementRepository procurementRepo;

	private static final Logger log = LoggerFactory.getLogger(FinanceService.class);

	public FinanceService(SaleRepository saleRepo, ProcurementRepository procurementRepo) {

		this.saleRepo = saleRepo;

		this.procurementRepo = procurementRepo;
	}

	public Map<String, Object> dashboard() {

		log.info("Finance dashboard calculation started");

		Map<String, Object> map = new HashMap<>();

		List<Sale> sales = saleRepo.findAll();

		List<Procurement> procurements = procurementRepo.findAll();

		log.info("Fetched sales records : {}", sales.size());

		log.info("Fetched procurement records : {}", procurements.size());

		BigDecimal todaySales = BigDecimal.ZERO;

		BigDecimal totalSales = BigDecimal.ZERO;

		BigDecimal cashCollected = BigDecimal.ZERO;

		BigDecimal upiCollected = BigDecimal.ZERO;

		BigDecimal cardCollected = BigDecimal.ZERO;

		// Sales Calculation

		for (Sale s : sales) {

			BigDecimal grand = s.getGrandTotal() == null ? BigDecimal.ZERO : s.getGrandTotal();

			totalSales = totalSales.add(grand);

			if (s.getCreatedAt() != null && s.getCreatedAt().toLocalDate().equals(LocalDate.now())) {

				todaySales = todaySales.add(grand);
			}

			cashCollected = cashCollected.add(

					s.getCashPaid() == null ? BigDecimal.ZERO : s.getCashPaid());

			upiCollected = upiCollected.add(

					s.getUpiPaid() == null ? BigDecimal.ZERO : s.getUpiPaid());

			cardCollected = cardCollected.add(

					s.getCardPaid() == null ? BigDecimal.ZERO : s.getCardPaid());
		}

		log.info("Sales calculation completed | Total Sales : {}", totalSales);

		// Payable procurement

		BigDecimal payable = BigDecimal.ZERO;

		for (Procurement p : procurements) {

			payable = payable.add(

					p.getDueAmount() == null ? BigDecimal.ZERO : p.getDueAmount());
		}

		log.info("Calculated payable amount : {}", payable);

		BigDecimal netBusiness = totalSales.subtract(payable);

		log.info("Net business calculated : {}", netBusiness);

		map.put("todaySales", todaySales);

		map.put("totalSales", totalSales);

		map.put("cashCollected", cashCollected);

		map.put("upiCollected", upiCollected);

		map.put("cardCollected", cardCollected);

		map.put("payable", payable);

		map.put("netBusiness", netBusiness);

		log.info("Finance dashboard loaded successfully");

		log.info("Cash : {} | UPI : {} | Card : {}", cashCollected, upiCollected, cardCollected);

		return map;
	}
}