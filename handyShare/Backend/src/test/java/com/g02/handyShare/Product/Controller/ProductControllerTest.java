package com.g02.handyShare.Product.Controller;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addProducts_ShouldReturnCreatedStatus_WhenProductIsAddedSuccessfully() {
        // Arrange
        Product product = new Product();
        product.setName("Drill");
        MultipartFile file = mock(MultipartFile.class);

        ResponseEntity<?> savedProductResponse = ResponseEntity.status(HttpStatus.CREATED).body(product);

        // Using doReturn instead of when().thenReturn() to avoid type mismatch issues
        doReturn(savedProductResponse).when(productService).addProduct(any(Product.class), any(MultipartFile.class));

        // Act
        ResponseEntity<?> response = productController.addProducts(product, file);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productService, times(1)).addProduct(any(Product.class), any(MultipartFile.class));
    }

    @Test
    void viewProductById_ShouldReturnProduct_WhenProductExists() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Drill");

        doReturn(product).when(productService).getProductById(1L);

        // Act
        Product result = productController.viewProductById(1L);

        // Assert
        assertEquals("Drill", result.getName());
        assertEquals(1L, result.getId());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void getAllCategories_ShouldReturnListOfProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Drill");
        Product product2 = new Product();
        product2.setName("Saw");

        List<Product> productList = Arrays.asList(product1, product2);
        doReturn(productList).when(productService).getAllProducts();

        // Act
        List<Product> result = productController.getAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Drill", result.get(0).getName());
        assertEquals("Saw", result.get(1).getName());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void deleteProduct_ShouldReturnSuccessMessage_WhenProductIsDeleted() {
        // Arrange
        Long productId = 1L;
        doReturn(true).when(productService).deleteProduct(productId);

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product deleted Successfully!", response.getBody());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void deleteProduct_ShouldReturnNotFoundMessage_WhenProductDoesNotExist() {
        // Arrange
        Long productId = 1L;
        doReturn(false).when(productService).deleteProduct(productId);

        // Act
        ResponseEntity<?> response = productController.deleteProduct(productId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product ID does not exist!", response.getBody());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void getNewlyAddedProductsByCategory_ShouldReturnProductsList() {
        // Arrange
        String category = "Tools";
        Product product1 = new Product();
        product1.setName("Drill");

        List<Product> products = Arrays.asList(product1);
        doReturn(products).when(productService).getNewlyAddedProductsByCategory(eq(category));

        // Act
        ResponseEntity<List<Product>> response = productController.getNewlyAddedProductsByCategory(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Drill", response.getBody().get(0).getName());
        verify(productService, times(1)).getNewlyAddedProductsByCategory(category);
    }
}
