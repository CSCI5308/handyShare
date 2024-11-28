package com.g02.handyShare.Product.Service;

import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("john@gmail.com");
    }

    @Test
    void testAddProduct_Positive() throws Exception {
        Product product = new Product();
        User user = new User();
        MultipartFile file = null; // Mock file

        mockSecurityContext("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(firebaseService.uploadFile(file, "product_images")).thenReturn("image_url");
        when(productRepository.save(product)).thenReturn(product);

        ResponseEntity<?> response = productService.addProduct(product, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testGetProductById_Positive() {
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(product, result);
    }

    @Test
    void testGetProductById_Negative() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testDeleteProduct_Positive() {
        when(productRepository.existsById(1L)).thenReturn(true);

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_Negative() {
        when(productRepository.existsById(1L)).thenReturn(false);

        boolean result = productService.deleteProduct(1L);

        assertFalse(result);
        verify(productRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testChangeAvailability() {
        // Arrange
        String email = "john@gmail.com";
        User owner = new User();
        owner.setId(1L);
        owner.setEmail(email);

        Product product = new Product();
        product.setCategory("Electronics");
        product.setId(10L);
        product.setName("Laptop");
        product.setRentalPrice(15.0);
        product.setAvailable(true); // Initial status
        product.setLender(owner);

        Boolean newStatus = false; // New availability status

        // Mock behavior
        when(authentication.getName()).thenReturn(owner.getEmail());
        when(userRepository.findByEmail(email)).thenReturn(owner);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        // Act
        ResponseEntity<?> response = productService.changeAvailability(product.getId(), newStatus);

        // Assert
        assertEquals("Product availability updated successfully.", response.getBody());
        assertEquals(newStatus, product.getAvailable()); // Verify that the status was updated
    }

    private void mockSecurityContext(String email) {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
    }
}
