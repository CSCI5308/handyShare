package com.g02.handyShare.Product.Service;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import com.g02.handyShare.Config.Firebase.FirebaseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

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

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addProduct_ShouldReturnSavedProduct_WhenValidRequest() throws IOException {
        Product product = new Product();
        product.setName("Drill");
        product.setDescription("Power drill");
        product.setRentalPrice(100.0);

        MultipartFile file = mock(MultipartFile.class);
        User user = new User();
        user.setEmail("test@example.com");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(firebaseService.uploadFile(file, "product_images")).thenReturn("image_url");
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<?> response = productService.addProduct(product, file);

        assertEquals(200, response.getStatusCodeValue());
        Product savedProduct = (Product) response.getBody();
        assertNotNull(savedProduct);
        assertEquals("Drill", savedProduct.getName());
        assertEquals("image_url", savedProduct.getProductImage());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_ShouldReturnError_WhenFileUploadFails() throws IOException {
        Product product = new Product();
        product.setName("Drill");

        MultipartFile file = mock(MultipartFile.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");
        when(firebaseService.uploadFile(file, "product_images")).thenThrow(new IOException("Upload failed"));

        ResponseEntity<?> response = productService.addProduct(product, file);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("ERROR WHILE ADDING YOUR PRODUCT, PLEASE TRY AGAIN AFTER SOME TIME.", response.getBody());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Drill");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
        assertEquals("Drill", foundProduct.getName());
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getProductById(1L));
        assertEquals("Product not Found!", exception.getMessage());
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        Product product1 = new Product();
        product1.setName("Drill");
        Product product2 = new Product();
        product2.setName("Saw");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        assertEquals("Drill", products.get(0).getName());
        assertEquals("Saw", products.get(1).getName());
    }

    @Test
    void deleteProduct_ShouldReturnTrue_WhenProductExists() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        boolean result = productService.deleteProduct(productId);

        assertTrue(result);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void deleteProduct_ShouldReturnFalse_WhenProductDoesNotExist() {
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(false);

        boolean result = productService.deleteProduct(productId);

        assertFalse(result);
        verify(productRepository, never()).deleteById(productId);
    }

    @Test
    void getNewlyAddedProductsByCategory_ShouldReturnProductsFromLastWeek() {
        String category = "Tools";
        Product product1 = new Product();
        product1.setName("Drill");

        when(productRepository.findNewlyAddedProductsByCategory(eq(category), any(LocalDateTime.class)))
            .thenReturn(Arrays.asList(product1));

        List<Product> products = productService.getNewlyAddedProductsByCategory(category);

        assertEquals(1, products.size());
        assertEquals("Drill", products.get(0).getName());
    }
}
