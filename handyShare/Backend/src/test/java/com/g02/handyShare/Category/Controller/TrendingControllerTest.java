package com.g02.handyShare.Category.Controller;

import com.g02.handyShare.Category.DTO.ProductIDRequest;
import com.g02.handyShare.Category.Entity.Trending;
import com.g02.handyShare.Category.Service.TrendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TrendingControllerTest {

    @Mock
    private TrendingService service;

    @InjectMocks
    private TrendingController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTrendingByCategory_Positive() {
        // Arrange
        String category = "Electronics";
        List<Trending> mockTrendingList = new ArrayList<>();
        mockTrendingList.add(new Trending()); // Adding a mock Trending object
        ResponseEntity<List<Trending>> mockResponse = ResponseEntity.ok(mockTrendingList);
        doReturn(mockResponse).when(service).fetchTrendingsByCategory(category);

        // Act
        ResponseEntity<?> response = controller.getTrendingByCategory(category);

        // Assert
        assertEquals(mockResponse.getStatusCode(), response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).fetchTrendingsByCategory(category);
    }

    @Test
    void testGetTrendingByCategory_Negative() {
        // Arrange
        String category = "InvalidCategory";
        doThrow(new RuntimeException("No products found")).when(service).fetchTrendingsByCategory(category);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.getTrendingByCategory(category));
        assertEquals("No products found", exception.getMessage());
        verify(service, times(1)).fetchTrendingsByCategory(category);
    }

    @Test
    void testAddToTrending_Positive() {
        // Arrange
        ProductIDRequest request = new ProductIDRequest();
        request.setProduct_id(1L);
        Trending mockTrending = new Trending(); // Mock Trending object
        doReturn(mockTrending).when(service).addToTrending(request.getProduct_id());

        // Act
        ResponseEntity<?> response = controller.addToTrending(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Trending);
        verify(service, times(1)).addToTrending(request.getProduct_id());
    }

    @Test
    void testAddToTrending_Negative() {
        // Arrange
        ProductIDRequest request = new ProductIDRequest();
        request.setProduct_id(99L);
        doThrow(new RuntimeException("Product not found")).when(service).addToTrending(request.getProduct_id());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> controller.addToTrending(request));
        assertEquals("Product not found", exception.getMessage());
        verify(service, times(1)).addToTrending(request.getProduct_id());
    }
}