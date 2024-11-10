package com.g02.handyShare.productService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.Product.Service.ProductService;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class productServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up the SecurityContext to return the mocked Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Ensure that the authentication mock is not null and configured correctly
        when(authentication.getName()).thenReturn("john@gmail.com");
    }

    @Test
    public void getProductById() {
        // Arrange
        Product product = new Product();
        product.setCategory("Electronics");
        product.setId(12L);
        product.setName("USB");

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // Act
        Product actual = productService.getProductById(product.getId());

        // Assert
        assertEquals(product, actual);
    }

    @Test
    public void addProductWithUserTest() throws IOException {
        // Arrange
        String email = "john@gmail.com";
        User owner = new User(1L, "John", email, "password123", "user", false, null, "123 Main St", "123456", "1234567890", null, email, null);

        when(authentication.getName()).thenReturn(owner.getEmail());
        when(userRepository.findByEmail(email)).thenReturn(owner);

        Product product = new Product();
        product.setCategory("Electronics");
        product.setName("Laptop");
        product.setRentalPrice(15.0);

        when(productRepository.save(product)).thenReturn(product);

        // Act
        ResponseEntity<?> response = productService.addProduct(product, null);

        // Assert
        assertEquals(product, response.getBody());
        Product object = (Product) response.getBody();
        assertEquals(owner, object.getLender()); //This will make sure that lender that we attached is the same that service return.
    }
}
