package com.g02.handyShare.Payment.Service;

import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRequest paymentRequest;

    @Mock
    private PaymentMethod paymentMethod;

    @Mock
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCheckoutSession_Success() throws StripeException {
        Map<String, Object> response = paymentService.createCheckoutSession(5000L, "usd");

        assertNotNull(response);
        assertTrue(response.containsKey("url"));
    }

    @Test
    void createCheckoutSession_StripeException() {
        try {
            Map<String, Object> response = paymentService.createCheckoutSession(-5000L, "usd");
            assertTrue(response.containsKey("error"));
        } catch (Exception e) {
            fail("StripeException was not handled properly");
        }
    }

    @Test
    void onboardCustomer_Success() throws StripeException {
        PaymentRequest request = new PaymentRequest(5000, "usd", "Jane Doe", "jane.doe@example.com", null);
        Map<String, Object> response = paymentService.onboardCustomer(request);

        assertNotNull(response);
        assertEquals("Customer onboarded successfully in test mode.", response.get("message"));
        assertNotNull(response.get("customerId"));
    }

    @Test
    void testSaveCardSuccess() throws StripeException {
        // Create a PaymentRequest mock with valid data
        when(paymentRequest.getCustomerId()).thenReturn("cus_RFAOuhzCkwbBsN");
        when(paymentRequest.getCardNumber()).thenReturn("4242424242424242");
        when(paymentRequest.getExpiryMonth()).thenReturn(12);
        when(paymentRequest.getExpiryYear()).thenReturn(2024);
        when(paymentRequest.getCvc()).thenReturn("123");

        when(customer.getId()).thenReturn("cus_RFAOuhzCkwbBsN");

        // Call the method
        Map<String, Object> response = paymentService.saveCard(paymentRequest);

        // Verify the response
        assertNotNull(response);
        assertEquals("Card saved successfully and set as default.", response.get("message"));
    }

    @Test
    void saveCard_InvalidCustomer_ThrowsException() {
        PaymentRequest request = new PaymentRequest();

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.saveCard(request);
        });
    }

    @Test
    void testSaveCardFailureNoCustomerId() {
        // Set up the mock PaymentRequest to return null for customerId
        when(paymentRequest.getCustomerId()).thenReturn(null);

        // Run the test and expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.saveCard(paymentRequest);
        });

        // Verify the exception message
        assertEquals("CustomerId is required to save card.", exception.getMessage());
    }
}