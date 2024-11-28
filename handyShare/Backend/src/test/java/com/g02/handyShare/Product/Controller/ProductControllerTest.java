package com.g02.handyShare.Product.Controller;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Service.CustomException;
import com.g02.handyShare.Product.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    ProductControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProducts_Positive() {
        Product product = new Product();
        MultipartFile file = null;

        doReturn(ResponseEntity.ok(product)).when(productService).addProduct(any(Product.class), any(MultipartFile.class));

        ResponseEntity<?> response = productController.addProducts(product, file);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testViewProductById_Positive() {
        Product product = new Product();
        product.setId(1L);

        doReturn(product).when(productService).getProductById(1L);

        ResponseEntity<?> response = productController.viewProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testViewProductById_Negative() {
        doReturn(null).when(productService).getProductById(1L);

        ResponseEntity<?> response = productController.viewProductById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    @Test
    void testDeleteProduct_Positive() {
        doReturn(true).when(productService).deleteProduct(1L);

        ResponseEntity<?> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product deleted Successfully!", response.getBody());
    }

    @Test
    void testDeleteProduct_Negative() {
        doReturn(false).when(productService).deleteProduct(1L);

        ResponseEntity<?> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product ID does not exist!", response.getBody());
    }

    @Test
    void testGetAllCategories() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> productList = List.of(product1, product2);

        doReturn(productList).when(productService).getAllProducts();

        ResponseEntity<List<Product>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productList, response.getBody());
    }

    @Test
    void testGetNewlyAddedProductsByCategory() {
        Product product = new Product();
        List<Product> productList = List.of(product);

        doReturn(productList).when(productService).getNewlyAddedProductsByCategory("Electronics");

        ResponseEntity<List<Product>> response = productController.getNewlyAddedProductsByCategory("Electronics");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productList, response.getBody());
    }

    @Test
    void testListProducts() {
        Product product = new Product();
        List<Product> productList = List.of(product);

        doReturn(productList).when(productService).listProductsForUser();

        ResponseEntity<List<Product>> response = productController.listProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productList, response.getBody());
    }

    @Test
    void testUpdateProduct_Positive() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");

        MultipartFile file = null; // Simulate no file

        doReturn(ResponseEntity.ok(product)).when(productService).updateProduct(eq(1L), any(Product.class), eq(file));

        ResponseEntity<?> response = productController.updateProduct(1L, product, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testUpdateProduct_Negative_NotFound() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");

        MultipartFile file = null;

        doThrow(new CustomException("Product not found")).when(productService).updateProduct(eq(1L), any(Product.class), eq(file));

        ResponseEntity<?> response = productController.updateProduct(1L, product, file);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    @Test
    void testUpdateProduct_Negative_InternalError() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");

        MultipartFile file = null;

        doThrow(new RuntimeException("Internal error")).when(productService).updateProduct(eq(1L), any(Product.class), eq(file));

        ResponseEntity<?> response = productController.updateProduct(1L, product, file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error while updating the product.", response.getBody());
    }

    @Test
    void testChangeAvailability_Positive() {
        Map<String, Boolean> statusMap = new HashMap<>();
        statusMap.put("status", true);

        doReturn(ResponseEntity.ok("Availability updated")).when(productService).changeAvailability(eq(1L), eq(true));

        ResponseEntity<?> response = productController.changeAvailability(1L, statusMap);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Availability updated", response.getBody());
    }

    @Test
    void testChangeAvailability_Negative() {
        Map<String, Boolean> statusMap = new HashMap<>();
        statusMap.put("status", false);

        doReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error changing availability"))
                .when(productService).changeAvailability(eq(1L), eq(false));

        ResponseEntity<?> response = productController.changeAvailability(1L, statusMap);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error changing availability", response.getBody());
    }

}