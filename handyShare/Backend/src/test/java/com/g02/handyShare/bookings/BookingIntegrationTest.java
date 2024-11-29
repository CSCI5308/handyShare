package com.g02.handyShare.bookings;

import com.g02.handyShare.CommonTestMethods;
import com.g02.handyShare.TestConstants;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.bookings.entity.Bookings;
import com.g02.handyShare.bookings.repository.BookingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookingIntegrationTest {

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

    @BeforeEach
    void setUp() {
        baseUrl = String.format(TestConstants.BASE_URL, port);
        Map<String, Object> result = CommonTestMethods.registerAndLogin(restTemplate, userRepository, baseUrl);
        token = (String) result.get("token");
        testUser = (User) result.get("user");
    }

    private Product createUniqueTestProduct() {
        Product product = new Product();
        product.setName("Laptop " + System.currentTimeMillis());
        product.setDescription("A unique product for testing");
        product.setRentalPrice(100.0);
        product.setAvailable(true);
        product.setCategory("Electronics");
        product.setLender(testUser);
        return productRepository.save(product);
    }

    @Test
    void testBorrowProduct() {
        Product testProduct = createUniqueTestProduct();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Bookings borrowRequest = new Bookings();
        borrowRequest.setProduct(testProduct);
        borrowRequest.setDuration(24);
        borrowRequest.setTimerStart(LocalDateTime.now().plusHours(1));

        HttpEntity<Bookings> request = new HttpEntity<>(borrowRequest, headers);

        ResponseEntity<Bookings> response = restTemplate.postForEntity(
                baseUrl + "/user/borrowProduct",
                request,
                Bookings.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testProduct.getId(), response.getBody().getProduct().getId());
        assertEquals(testUser.getId(), response.getBody().getBorrower().getId());
    }

    @Test
    void testGetBorrowedProducts() {
        Product testProduct = createUniqueTestProduct();
        testBorrowProduct(testProduct);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<List> response = restTemplate.exchange(
                baseUrl + "/user/borrowedProducts",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                List.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @DirtiesContext
    void testProductReturnedLender() {
        Product testProduct = createUniqueTestProduct();
        Bookings booking = testBorrowProduct(testProduct);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Long>> request = new HttpEntity<>(Map.of("borrowId", booking.getId()), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/user/product/ReturnedLender",
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("has been returned"));

        // Verify that the product is now available
        Product updatedProduct = productRepository.findById(testProduct.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertTrue(updatedProduct.getAvailable());
    }

    private Bookings testBorrowProduct(Product testProduct) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Bookings borrowRequest = new Bookings();
        borrowRequest.setProduct(testProduct);
        borrowRequest.setDuration(24);
        borrowRequest.setTimerStart(LocalDateTime.now().plusHours(1));

        HttpEntity<Bookings> request = new HttpEntity<>(borrowRequest, headers);

        ResponseEntity<Bookings> response = restTemplate.postForEntity(
                baseUrl + "/user/borrowProduct",
                request,
                Bookings.class
        );

        return response.getBody();
    }

    @AfterEach
    public void tearDown() {
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