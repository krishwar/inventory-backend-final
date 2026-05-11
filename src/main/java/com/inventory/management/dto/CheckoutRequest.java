package com.inventory.management.dto;

import java.util.List;

public class CheckoutRequest {

    private String customerName;
    private String customerMobile;

    private String paymentMode;

    private Double discount;
    private Double cashPaid;
    private Double upiPaid;
    private Double cardPaid;

    private List<CartItemDto> items;

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerMobile() { return customerMobile; }
    public void setCustomerMobile(String customerMobile) { this.customerMobile = customerMobile; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }

    public Double getCashPaid() { return cashPaid; }
    public void setCashPaid(Double cashPaid) { this.cashPaid = cashPaid; }

    public Double getUpiPaid() { return upiPaid; }
    public void setUpiPaid(Double upiPaid) { this.upiPaid = upiPaid; }

    public Double getCardPaid() { return cardPaid; }
    public void setCardPaid(Double cardPaid) { this.cardPaid = cardPaid; }

    public List<CartItemDto> getItems() { return items; }
    public void setItems(List<CartItemDto> items) { this.items = items; }
}