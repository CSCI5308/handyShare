package com.g02.handyShare.Payment.Controller;

import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.g02.handyShare.Payment.Service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPaymentIntent_ShouldReturnResponse_WhenValidRequest() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1000L);
        paymentRequest.setCurrency("usd");
        paymentRequest.setPaymentMethodId("pm_test");

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");

        when(paymentService.createPaymentIntent(paymentRequest)).thenReturn(mockResponse);

        // Act
        ResponseEntity<Map<String, Object>> responseEntity = paymentController.createPaymentIntent(paymentRequest);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("success", responseEntity.getBody().get("status"));
        verify(paymentService, times(1)).createPaymentIntent(paymentRequest);
    }

    @Test
    void createPaymentIntent_ShouldThrowException_WhenPaymentMethodIdIsMissing() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1000L);
        paymentRequest.setCurrency("usd");
        paymentRequest.setPaymentMethodId(null);  // No payment method ID

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> paymentController.createPaymentIntent(paymentRequest));
        assertEquals("Payment method ID must be provided.", exception.getMessage());
        verify(paymentService, never()).createPaymentIntent(any(PaymentRequest.class));
    }

    @Test
    void handleOptions_ShouldExecuteSuccessfully() {
        // This method doesn't return anything, so we're just calling it to ensure no exceptions are thrown
        paymentController.handleOptions();
    }
}
