package com.g02.handyShare.Payment;

import com.g02.handyShare.CommonTestMethods;
import com.g02.handyShare.TestConstants;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;
import com.g02.handyShare.Payment.Request.PaymentRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PaymentIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private String baseUrl;
    private String token;
    private User testUser;
    private Product testProduct;
    private Bookings testBooking;

    @BeforeEach
    void setUp() {
        baseUrl = String.format(TestConstants.BASE_URL, port);
        // Register and login a test user
        Map<String, Object> result = CommonTestMethods.registerAndLogin(restTemplate, userRepository, baseUrl);
        token = (String) result.get("token");
        testUser = (User) result.get("user");

        // Create a test product
        testProduct = createTestProduct();

        // Create a test booking
        testBooking = createTestBooking(testProduct);
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setName("Test Product " + System.currentTimeMillis());
        product.setDescription("Test product for payment integration");
        product.setRentalPrice(100.0);
        product.setAvailable(true);
        product.setCategory("Electronics");
        product.setLender(testUser);
        return productRepository.save(product);
    }

    private Bookings createTestBooking(Product product) {
        Bookings booking = new Bookings();
        booking.setProduct(product);
        booking.setBorrower(testUser);
        booking.setDuration(24);
        booking.setTimerStart(LocalDateTime.now().plusHours(1));
        booking.setTimerEnd(LocalDateTime.now().plusHours(25));
        return bookingRepository.save(booking);
    }

    @Test
    void testCompletePaymentFlow() {
        // Step 1: Create customer onboarding (previous code remains the same)
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        PaymentRequest onboardRequest = new PaymentRequest();
        onboardRequest.setEmail(testUser.getEmail());
        onboardRequest.setName(testUser.getName());

        HttpEntity<PaymentRequest> onboardingRequest = new HttpEntity<>(onboardRequest, headers);

        ResponseEntity<Map> onboardingResponse = restTemplate.postForEntity(
                baseUrl + "/all/payment/onboard",
                onboardingRequest,
                Map.class
        );

        assertEquals(HttpStatus.OK, onboardingResponse.getStatusCode());
        assertNotNull(onboardingResponse.getBody().get("customerId"));
        String customerId = (String) onboardingResponse.getBody().get("customerId");

        // Step 2: Save card details (previous code remains the same)
        PaymentRequest cardRequest = new PaymentRequest();
        cardRequest.setCustomerId(customerId);
        cardRequest.setCardNumber("4242424242424242");
        cardRequest.setCvc("123");
        cardRequest.setExpiryMonth(12);
        cardRequest.setExpiryYear(2025);

        HttpEntity<PaymentRequest> saveCardRequest = new HttpEntity<>(cardRequest, headers);

        ResponseEntity<Map> saveCardResponse = restTemplate.postForEntity(
                baseUrl + "/all/payment/save-card",
                saveCardRequest,
                Map.class
        );

        assertEquals(HttpStatus.OK, saveCardResponse.getStatusCode());
        assertNotNull(saveCardResponse.getBody().get("paymentMethodId"));

        // Step 3: Create checkout session with modified amount handling
        // Calculate total amount including rental price and any fees
        double rentalAmount = testProduct.getRentalPrice();
        long amountInCents = (long) (rentalAmount * 100); // Convert to cents for Stripe

        Map<String, Object> checkoutRequest = new HashMap<>();
        checkoutRequest.put("amount", amountInCents);
        checkoutRequest.put("currency", "usd");

        // Add debug logging
        System.out.println("Checkout Request - Amount in cents: " + amountInCents);
        System.out.println("Checkout Request - Currency: " + checkoutRequest.get("currency"));

        HttpEntity<Map<String, Object>> checkoutSessionRequest = new HttpEntity<>(checkoutRequest, headers);

        ResponseEntity<Map> checkoutResponse = restTemplate.postForEntity(
                baseUrl + "/all/payment/checkout-session",
                checkoutSessionRequest,
                Map.class
        );

        // Add debug logging for response
        if (checkoutResponse.getStatusCode() != HttpStatus.OK) {
            System.out.println("Checkout Response Error: " + checkoutResponse.getBody());
        }

        assertEquals(HttpStatus.OK, checkoutResponse.getStatusCode());
        assertNotNull(checkoutResponse.getBody());
        assertNotNull(checkoutResponse.getBody().get("url"));

        // Step 4: Verify borrowed products listing (previous code remains the same)
        ResponseEntity<List> borrowedProductsResponse = restTemplate.exchange(
                baseUrl + "/user/borrowedProducts",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        );

        assertEquals(HttpStatus.OK, borrowedProductsResponse.getStatusCode());
        assertNotNull(borrowedProductsResponse.getBody());
        assertTrue(borrowedProductsResponse.getBody().size() > 0);
    }

    @Test
    void testCheckoutSessionWithInvalidAmount() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> checkoutRequest = new HashMap<>();
        checkoutRequest.put("amount", -100); // Invalid amount
        checkoutRequest.put("currency", "usd");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(checkoutRequest, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl + "/all/payment/checkout-session",
                request,
                Map.class
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @AfterEach
    void tearDown() {
        // Find all bookings related to the test user and delete those bookings
        List<Bookings> userBookings = bookingRepository.findAllByBorrowerId(testUser.getId());
        bookingRepository.deleteAll(userBookings);

        // Delete all products created by the test user
        List<Product> userProducts = productRepository.findByLenderEmail(testUser.getEmail());
        productRepository.deleteAll(userProducts);

        // Delete the test user
        userRepository.delete(testUser);
    }
}