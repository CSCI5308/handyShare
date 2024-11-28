package com.g02.handyShare.Category.Service;

import com.g02.handyShare.Category.Entity.Trending;
import com.g02.handyShare.Category.Repository.TrendingRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrendingServiceTest {

    @Mock
    private TrendingRepository trendingRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private TrendingService trendingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchTrendingsByCategory_Success() {
        // Arrange
        String category = "Tools";
        Product product1 = new Product(1L, "Hammer", "Description", category, "hammer.jpg", 10.0, null, null, true);
        Product product2 = new Product(2L, "Drill", "Description", category, "drill.jpg", 20.0, null, null, true);

        Trending trending1 = new Trending(1L, product1);
        Trending trending2 = new Trending(2L, product2);

        when(trendingRepository.findByProductCategory(category)).thenReturn(Arrays.asList(trending1, trending2));

        // Act
        ResponseEntity<?> response = trendingService.fetchTrendingsByCategory(category);

        // Assert
        assertNotNull(response);
        List<Trending> trendingList = (List<Trending>) response.getBody();
        assertNotNull(trendingList);
        assertEquals(2, trendingList.size());
        verify(trendingRepository, times(1)).findByProductCategory(category);
    }

    @Test
    void testFetchTrendingsByCategory_NoResults() {
        // Arrange
        String category = "NonExistentCategory";

        when(trendingRepository.findByProductCategory(category)).thenReturn(Arrays.asList());

        // Act
        ResponseEntity<?> response = trendingService.fetchTrendingsByCategory(category);

        // Assert
        assertNotNull(response);
        List<Trending> trendingList = (List<Trending>) response.getBody();
        assertNotNull(trendingList);
        assertEquals(0, trendingList.size());
        verify(trendingRepository, times(1)).findByProductCategory(category);
    }

    @Test
    void testAddToTrending_Success() {
        // Arrange
        Long productId = 1L;
        Product product = new Product(1L, "Hammer", "Description", "Tools", "hammer.jpg", 10.0, null, null, true);
        Trending expectedTrending = new Trending(1L, product);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(trendingRepository.save(any(Trending.class))).thenReturn(expectedTrending);

        // Act
        Trending result = trendingService.addToTrending(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getProduct().getId());
        verify(productRepository, times(1)).findById(productId);
        verify(trendingRepository, times(1)).save(any(Trending.class));
    }

    @Test
void testAddToTrending_ProductNotFound() {
    // Arrange
    Long productId = 999L;

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    // Act & Assert
    RuntimeException exception = assertThrows(RuntimeException.class, () -> trendingService.addToTrending(productId));
    assertEquals("Something went wrong while adding the product to the trending table: Product not found with id: " + productId, exception.getMessage());
    verify(productRepository, times(1)).findById(productId);
    verify(trendingRepository, never()).save(any(Trending.class));
}

}
