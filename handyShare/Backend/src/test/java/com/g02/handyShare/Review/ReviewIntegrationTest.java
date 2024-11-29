package com.g02.handyShare.Review;

import com.g02.handyShare.CommonTestMethods;
import com.g02.handyShare.TestConstants;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;
import com.g02.handyShare.Review.Entity.Review;
import com.g02.handyShare.Review.Repository.ReviewRepository;
import com.g02.handyShare.Review.Dto.ReviewResponse;
import com.g02.handyShare.Review.Dto.ReviewWithUserDTO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReviewIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

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

        // Create and complete a booking
        testBooking = createAndCompleteBooking();
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setName("Test Product " + System.currentTimeMillis());
        product.setDescription("Test product for review integration");
        product.setRentalPrice(100.0);
        product.setAvailable(true);
        product.setCategory("Electronics");
        product.setLender(testUser);
        return productRepository.save(product);
    }

    private Bookings createAndCompleteBooking() {
        // Create booking
        Bookings booking = new Bookings();
        booking.setProduct(testProduct);
        booking.setBorrower(testUser);
        booking.setDuration(24);
        booking.setTimerStart(LocalDateTime.now().minusHours(25)); // Started 25 hours ago
        booking.setTimerEnd(LocalDateTime.now().minusHours(1)); // Ended 1 hour ago
        booking.setReturnDateTime(LocalDateTime.now()); // Returned now
        booking.setTotalPayment(100.0);

        return bookingRepository.save(booking);
    }

    @Test
    void testCreateReviewForBorrowedProduct() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("userId", testUser.getId());
        body.add("productId", testProduct.getId());
        body.add("reviewText", "Great product, works perfectly!");
        body.add("rating", 5);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ReviewResponse> response = restTemplate.postForEntity(
                baseUrl + "/user/review-create",
                requestEntity,
                ReviewResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Review created successfully", response.getBody().getMessage());
        assertNotNull(response.getBody().getReview());
        assertEquals(5, response.getBody().getReview().getRating());
        assertEquals(testProduct.getId(), response.getBody().getReview().getProductId());
    }

    @Test
    void testCreateReviewWithImageForBorrowedProduct() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("userId", testUser.getId());
        body.add("productId", testProduct.getId());
        body.add("reviewText", "Amazing product with photo evidence!");
        body.add("rating", 4);
        body.add("image", createMockProfileImage());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<ReviewResponse> response = restTemplate.postForEntity(
                baseUrl + "/user/review-create",
                requestEntity,
                ReviewResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getReview().getImage());
    }

    @Test
    void testGetReviewsForProduct() {
        // First create a review for the borrowed product
        testCreateReviewForBorrowedProduct();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<List<ReviewWithUserDTO>> response = restTemplate.exchange(
                baseUrl + "/user/review-product/" + testProduct.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<ReviewWithUserDTO>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(testUser.getName(), response.getBody().get(0).getUsername());
    }

    @Test
    void testGetReviewsForUser() {
        // First create a review for the borrowed product
        testCreateReviewForBorrowedProduct();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<List<Review>> response = restTemplate.exchange(
                baseUrl + "/user/review-user/" + testUser.getId(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Review>>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(testUser.getId(), response.getBody().get(0).getUserId());
    }

    private ByteArrayResource createMockProfileImage() {
        byte[] fileContent = "Mock Image Content".getBytes(); // Mock content for testing
        return new ByteArrayResource(fileContent) {
            @Override
            public String getFilename() {
                return "reviewImage.jpg";
            }
        };
    }

    @AfterEach
    void tearDown() {
        // Clean up review test data
        reviewRepository.deleteAll();

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