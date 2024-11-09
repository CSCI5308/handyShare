package com.g02.handyShare.Payment.Service;


import com.g02.handyShare.Payment.Request.PaymentRequest;
import com.stripe.exception.ApiException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentIntent paymentIntent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPaymentIntent_ShouldReturnClientSecret_WhenValidRequest() throws Exception {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1000L);
        paymentRequest.setCurrency("usd");
        paymentRequest.setPaymentMethodId("pm_test");

        // Mock PaymentIntent response
        when(paymentIntent.getClientSecret()).thenReturn("test_client_secret");

        // Mock static PaymentIntent.create to return the mocked paymentIntent
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("clientSecret", "test_client_secret");

        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            mockedPaymentIntent.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(paymentIntent);

            // Act
            Map<String, Object> result = paymentService.createPaymentIntent(paymentRequest);

            // Assert
            assertEquals(responseData, result);
            verify(paymentIntent, times(1)).getClientSecret();
        }
    }

    @Test
    void createPaymentIntent_ShouldThrowException_WhenPaymentRequestIsInvalid() {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1000L);

        // Act & Assert
        assertThrows(Exception.class, () -> paymentService.createPaymentIntent(paymentRequest));
    }

    @Test
    void createPaymentIntent_ShouldThrowStripeException_WhenStripeFails() throws ApiException {
        // Arrange
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1000L);
        paymentRequest.setCurrency("usd");
        paymentRequest.setPaymentMethodId("pm_test");

        // Mock static PaymentIntent.create to throw an ApiException
        try (MockedStatic<PaymentIntent> mockedPaymentIntent = mockStatic(PaymentIntent.class)) {
            mockedPaymentIntent.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class)))
                .thenThrow(new ApiException("Stripe error", null, null, 400, null));

            // Act & Assert
            ApiException exception = assertThrows(ApiException.class, () -> paymentService.createPaymentIntent(paymentRequest));
            assertEquals("Stripe error", exception.getMessage());
        }
    }
}
