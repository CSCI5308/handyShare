// package com.g02.handyShare.Payment.Service;

// import com.g02.handyShare.Payment.Request.PaymentRequest;
// import com.stripe.model.checkout.Session;
// import com.stripe.model.Customer;
// import com.stripe.model.PaymentMethod;
// import com.stripe.exception.StripeException;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.springframework.beans.factory.annotation.Value;

// import java.util.HashMap;
// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;

// public class PaymentServiceTest {

//     @InjectMocks
//     private PaymentService paymentService;

//     @Mock
//     private PaymentRequest paymentRequest;

//     @Mock
//     private Session session;

//     @Mock
//     private Customer customer;

//     @Mock
//     private PaymentMethod paymentMethod;

//     @BeforeEach
//     public void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     public void testCreateCheckoutSessionSuccess() {
//         // Prepare mock data
//         Long amount = 5000L; // 50.00 in cents
//         String currency = "usd";

//         // Mock Stripe behavior
//         when(session.getUrl()).thenReturn("http://localhost:3000/checkout-session-url");

//         // Call the method
//         Map<String, Object> response = paymentService.createCheckoutSession(amount, currency);

//         // Verify
//         assertTrue(response.containsKey("url"));
//         assertEquals("http://localhost:3000/checkout-session-url", response.get("url"));
//     }

//     @Test
//     public void testCreateCheckoutSessionFailure() {
//         // Prepare mock data
//         Long amount = 5000L; // 50.00 in cents
//         String currency = "usd";

//         // Simulate StripeException
//         when(session.getUrl()).thenThrow(StripeException.class);

//         // Call the method
//         Map<String, Object> response = paymentService.createCheckoutSession(amount, currency);

//         // Verify error message
//         assertTrue(response.containsKey("error"));
//         assertTrue(response.get("error").toString().contains("Failed to create checkout session"));
//     }

//     @Test
// public void testOnboardCustomerSuccess() throws StripeException {
//     // Mock PaymentRequest
//     when(paymentRequest.getEmail()).thenReturn("testuser@example.com");
//     when(paymentRequest.getName()).thenReturn("Test User");

//     // Create a map with customer parameters to mock Customer.create()
//     Map<String, Object> customerParams = new HashMap<>();
//     customerParams.put("email", "testuser@example.com");
//     customerParams.put("name", "Test User");

//     // Mock Stripe behavior
//     when(Customer.create(eq(customerParams))).thenReturn(customer);
//     when(customer.getId()).thenReturn("cus_test123");

//     // Call the method
//     Map<String, Object> response = paymentService.onboardCustomer(paymentRequest);

//     // Verify
//     assertTrue(response.containsKey("customerId"));
//     assertEquals("cus_test123", response.get("customerId"));
//     assertEquals("Customer onboarded successfully in test mode.", response.get("message"));
// }


// @Test
// public void testOnboardCustomerFailure() throws StripeException {
//     // Simulate StripeException
//     when(paymentRequest.getEmail()).thenReturn("testuser@example.com");
//     when(paymentRequest.getName()).thenReturn("Test User");

//     // Create a map with customer parameters to mock Customer.create()
//     Map<String, Object> customerParams = new HashMap<>();
//     customerParams.put("email", "testuser@example.com");
//     customerParams.put("name", "Test User");

//     // Mock Stripe behavior to throw an exception
//     when(Customer.create(eq(customerParams))).thenThrow(StripeException.class);

//     // Call the method
//     RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.onboardCustomer(paymentRequest));

//     // Verify exception message
//     assertTrue(exception.getMessage().contains("Error onboarding customer"));
// }

//     @Test
//     public void testSaveCardSuccess() throws StripeException {
//         // Mock PaymentRequest
//         when(paymentRequest.getCustomerId()).thenReturn("cus_test123");
//         when(paymentRequest.getCardNumber()).thenReturn("4242424242424242");
//         when(paymentRequest.getExpiryMonth()).thenReturn(12);
//         when(paymentRequest.getExpiryYear()).thenReturn(2024);
//         when(paymentRequest.getCvc()).thenReturn("123");
    
//         // Create a map with the required parameters for the PaymentMethod.create call
//         Map<String, Object> paymentMethodParams = new HashMap<>();
//         paymentMethodParams.put("type", "card");
//         paymentMethodParams.put("card", Map.of(
//             "number", "4242424242424242",
//             "exp_month", 12,
//             "exp_year", 2024,
//             "cvc", "123"
//         ));
    
//         // Mock Stripe behavior for creating PaymentMethod using the updated method
//         when(PaymentMethod.create(paymentMethodParams)).thenReturn(paymentMethod);
//         when(paymentMethod.getId()).thenReturn("pm_test123");
    
//         // Mock Customer behavior
//         when(Customer.retrieve(any())).thenReturn(customer);
    
//         // Create a mock Map to simulate customer update parameters
//         Map<String, Object> customerUpdateParams = new HashMap<>();
//         customerUpdateParams.put("invoice_settings", Map.of("default_payment_method", "pm_test123"));
    
//         // Mock the update method for the Customer object with the correct parameters
//         doNothing().when(customer).update(customerUpdateParams);
    
//         // Call the method
//         Map<String, Object> response = paymentService.saveCard(paymentRequest);
    
//         // Verify
//         assertTrue(response.containsKey("message"));
//         assertEquals("Card saved successfully and set as default.", response.get("message"));
//         assertEquals("pm_test123", response.get("paymentMethodId"));
//     }
    


//     @Test
//     public void testSaveCardFailureNoCustomerId() throws StripeException {
//         // Mock PaymentRequest with no customer ID
//         when(paymentRequest.getCustomerId()).thenReturn(null);

//         // Call the method
//         IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> paymentService.saveCard(paymentRequest));

//         // Verify exception message
//         assertEquals("CustomerId is required to save card.", exception.getMessage());
//     }

//     @Test
//     public void testSaveCardFailureStripeException() throws StripeException {
//         // Mock PaymentRequest
//         when(paymentRequest.getCustomerId()).thenReturn("cus_test123");
//         when(paymentRequest.getCardNumber()).thenReturn("4242424242424242");
//         when(paymentRequest.getExpiryMonth()).thenReturn(12);
//         when(paymentRequest.getExpiryYear()).thenReturn(2024);
//         when(paymentRequest.getCvc()).thenReturn("123");
    
//         // Create a map with payment method parameters to mock PaymentMethod.create()
//         Map<String, Object> paymentMethodParams = new HashMap<>();
//         paymentMethodParams.put("type", "card"); // Ensure 'type' is correctly set
//         paymentMethodParams.put("card", Map.of(
//             "number", "4242424242424242",
//             "exp_month", 12,
//             "exp_year", 2024,
//             "cvc", "123"
//         ));
    
//         // Mock StripeException (Mocking the exception explicitly)
//         StripeException stripeException = mock(StripeException.class);
    
//         // Simulate StripeException when creating PaymentMethod
//         when(PaymentMethod.create(eq(paymentMethodParams))).thenThrow(stripeException);
    
//         // Call the method
//         RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentService.saveCard(paymentRequest));
    
//         // Verify exception message
//         assertTrue(exception.getMessage().contains("Error saving card"));
//     }
    
    
// }
