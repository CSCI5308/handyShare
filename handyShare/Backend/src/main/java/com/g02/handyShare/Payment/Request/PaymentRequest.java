package com.g02.handyShare.Payment.Request;

public class PaymentRequest {
    private String paymentMethodId; // Payment method ID from Stripe
    private Long amount;             // Amount in cents
    private String currency;         // Currency code (e.g., "usd")

    // Default constructor
    public PaymentRequest() {}

    // Constructor to initialize amount and currency
    public PaymentRequest(int amount, String currency) {
        this.amount = (long) amount;  // Convert int to Long
        this.currency = currency;
    }

    // Getter for paymentMethodId
    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    // Setter for paymentMethodId
    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    // Getter for amount
    public Long getAmount() {
        return amount;
    }

    // Setter for amount
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    // Getter for currency
    public String getCurrency() {
        return currency;
    }

    // Setter for currency
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
