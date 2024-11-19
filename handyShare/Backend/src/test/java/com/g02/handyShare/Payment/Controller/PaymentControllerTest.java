
package com.g02.handyShare.Payment.Controller;

import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.g02.handyShare.Payment.Service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testCreateCheckoutSessionSuccess() throws Exception {
        // Mock the paymentService behavior
        Map<String, Object> response = new HashMap<>();
        response.put("checkoutSessionId", "session_test123");
        when(paymentService.createCheckoutSession(100L, "USD")).thenReturn(response);

        // Perform the request and verify the result
        mockMvc.perform(post("/api/v1/all/payment/checkout-session")
                        .contentType("application/json")
                        .content("{\"amount\": 100, \"currency\": \"USD\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkoutSessionId").value("session_test123"));
    }

    @Test
    public void testCreateCheckoutSessionFailure() throws Exception {
        // Mock the paymentService behavior
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Failed to create checkout session");
        when(paymentService.createCheckoutSession(100L, "USD")).thenReturn(response);

        // Perform the request and verify the result
        mockMvc.perform(post("/api/v1/all/payment/checkout-session")
                        .contentType("application/json")
                        .content("{\"amount\": 100, \"currency\": \"USD\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Failed to create checkout session"));
    }

    @Test
    public void testOnboardCustomerSuccess() throws Exception {
        // Create a mock PaymentRequest and response
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setEmail("testuser@example.com");
        paymentRequest.setName("Test User");

        Map<String, Object> response = new HashMap<>();
        response.put("customerId", "cus_test123");
        response.put("message", "Customer onboarded successfully");

        // Mock the paymentService behavior
        when(paymentService.onboardCustomer(any(PaymentRequest.class))).thenReturn(response);

        // Perform the request and verify the result
        mockMvc.perform(post("/api/v1/all/payment/onboard")
                        .contentType("application/json")
                        .content("{\"email\": \"testuser@example.com\", \"name\": \"Test User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("cus_test123"))
                .andExpect(jsonPath("$.message").value("Customer onboarded successfully"));
    }

    @Test
    public void testOnboardCustomerFailure() throws Exception {
        // Create a mock PaymentRequest and simulate a failure
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setEmail("testuser@example.com");
        paymentRequest.setName("Test User");

        // Mock the paymentService behavior to throw an exception
        when(paymentService.onboardCustomer(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException("Error onboarding customer"));

        // Perform the request and verify the result
        mockMvc.perform(post("/api/v1/all/payment/onboard")
                        .contentType("application/json")
                        .content("{\"email\": \"testuser@example.com\", \"name\": \"Test User\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error onboarding customer"));
    }

    @Test
    public void testSaveCardSuccess() throws Exception {
        // Create a mock PaymentRequest and response
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCustomerId("cus_test123");
        paymentRequest.setCardNumber("4242424242424242");
        paymentRequest.setExpiryMonth(12);
        paymentRequest.setExpiryYear(2024);
        paymentRequest.setCvc("123");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Card saved successfully and set as default.");
        response.put("paymentMethodId", "pm_test123");

        // Mock the paymentService behavior
        when(paymentService.saveCard(any(PaymentRequest.class))).thenReturn(response);

        // Perform the request and verify the result
        mockMvc.perform(post("/api/v1/all/payment/save-card")
                        .contentType("application/json")
                        .content("{\"customerId\": \"cus_test123\", \"cardNumber\": \"4242424242424242\", \"expiryMonth\": 12, \"expiryYear\": 2024, \"cvc\": \"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Card saved successfully and set as default."))
                .andExpect(jsonPath("$.paymentMethodId").value("pm_test123"));
    }

    @Test
    public void testSaveCardFailure() throws Exception {
        // Create a mock PaymentRequest and simulate a failure
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setCustomerId("cus_test123");
        paymentRequest.setCardNumber("4242424242424242");
        paymentRequest.setExpiryMonth(12);
        paymentRequest.setExpiryYear(2024);
        paymentRequest.setCvc("123");

        // Mock the paymentService behavior to throw an exception
        when(paymentService.saveCard(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException("Error saving card"));

        // Perform the request and verify the result
        mockMvc.perform(post("/api/v1/all/payment/save-card")
                        .contentType("application/json")
                        .content("{\"customerId\": \"cus_test123\", \"cardNumber\": \"4242424242424242\", \"expiryMonth\": 12, \"expiryYear\": 2024, \"cvc\": \"123\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error saving card"));
    }

    @Test
    public void testHandleOptionsRequest() throws Exception {
        // Perform the OPTIONS request to verify preflight handling
        mockMvc.perform(options("/api/v1/all/payment/checkout-session")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
