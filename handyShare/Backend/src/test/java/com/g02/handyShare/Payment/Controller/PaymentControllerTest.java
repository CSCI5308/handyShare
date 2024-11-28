package com.g02.handyShare.Payment.Controller;

import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.g02.handyShare.Payment.Service.PaymentService;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

class PaymentControllerTest {

    private PaymentController paymentController;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = Mockito.mock(PaymentService.class);
        paymentController = new PaymentController(paymentService);
    }

    @Test
    void createCheckoutSession_Success() throws Exception {
        Mockito.when(paymentService.createCheckoutSession(anyLong(), anyString()))
                .thenReturn(Map.of("url", "http://checkout.stripe.com/session_id"));

        ResponseEntity<Map<String, Object>> response = paymentController.createCheckoutSession(Map.of(
                "amount", "5000",
                "currency", "usd"
        ));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://checkout.stripe.com/session_id", response.getBody().get("url"));
    }

    @Test
    void createCheckoutSession_Failure() throws Exception {
        Mockito.when(paymentService.createCheckoutSession(anyLong(), anyString()))
                .thenReturn(Map.of("error", "Failed to create session"));

        ResponseEntity<Map<String, Object>> response = paymentController.createCheckoutSession(Map.of(
                "amount", "5000",
                "currency", "usd"
        ));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to create session", response.getBody().get("error"));
    }

    @Test
    void onboardCustomer_Success() {
        PaymentRequest paymentRequest = new PaymentRequest(5000, "usd", "John Doe", "john.doe@example.com", null);
        Mockito.when(paymentService.onboardCustomer(any(PaymentRequest.class)))
                .thenReturn(Map.of("customerId", "cus_test123"));

        ResponseEntity<Map<String, Object>> response = paymentController.onboardCustomer(paymentRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("cus_test123", response.getBody().get("customerId"));
    }

    @Test
    void saveCard_Success() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCustomerId("cus_test123");
        Mockito.when(paymentService.saveCard(any(PaymentRequest.class)))
                .thenReturn(Map.of("message", "Card saved successfully"));

        ResponseEntity<Map<String, Object>> response = paymentController.saveCard(paymentRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Card saved successfully", response.getBody().get("message"));
    }

    @Test
    void saveCard_CustomerIdMissing() throws StripeException {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCardNumber("4242424242424242");
        paymentRequest.setExpiryMonth(12);
        paymentRequest.setExpiryYear(2025);
        paymentRequest.setCvc("123");

        // Mock the behavior of the service to throw IllegalArgumentException when customerId is missing
        Mockito.when(paymentService.saveCard(Mockito.any(PaymentRequest.class)))
                .thenThrow(new IllegalArgumentException("CustomerId is required to save card."));


        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.saveCard(paymentRequest);
        });

        // Assert
        assertEquals("CustomerId is required to save card.", exception.getMessage());
    }


}