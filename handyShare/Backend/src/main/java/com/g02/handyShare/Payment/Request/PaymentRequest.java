package com.g02.handyShare.Payment.Request;

public class PaymentRequest {
    private String paymentMethodId; // Payment method ID from Stripe
    private Long amount;             // Amount in cents
    private String currency;         // Currency code (e.g., "usd")
    private String name;
    private String email;
    private String customerId;
    private String cardNumber;
    private String cvc;
    private Integer expiryMonth;
    private Integer expiryYear;

    // Default constructor
    public PaymentRequest() {}

    // Constructor to initialize amount and currency
    public PaymentRequest(int amount, String currency, String name, String email, String customerId) {
        this.amount = (long) amount;  // Convert int to Long
        this.currency = currency;
        this.name = name;
        this.email = email;
        this.customerId = customerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getCvc() {
        return cvc;
    }
    
    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
    
    public Integer getExpiryMonth() {
        return expiryMonth;
    }
    
    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }
    
    public Integer getExpiryYear() {
        return expiryYear;
    }
    
    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }
}