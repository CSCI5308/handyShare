//package com.g02.handyShare.Review.Service;
//
//import com.g02.handyShare.Review.Entity.Review;
//import com.g02.handyShare.Review.Repository.ReviewRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class ReviewServiceImplTest {
//
//    @Mock
//    private ReviewRepository reviewRepository;
//
//    @InjectMocks
//    private ReviewServiceImpl reviewService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetReviewsByProductId() {
//        // Arrange
//        Long productId = 1L;
//        Product mockProduct = new Product(); // Mock Product entity
//        mockProduct.setId(productId);       // Set the productId in the Product instance
//
//        List<Review> mockReviews = Arrays.asList(
//                new Review(1L, "Good Product", 5, mockProduct),
//                new Review(2L, "Could be better", 3, mockProduct)
//        );
//
//        when(reviewRepository.findByProductId(productId)).thenReturn(mockReviews);
//
//        // Act
//        List<Review> result = reviewService.getReviewsByProductId(productId);
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("Good Product", result.get(0).getComment());
//        assertEquals(productId, result.get(0).getProduct().getId());
//        verify(reviewRepository, times(1)).findByProductId(productId);
//    }
//
//
//    @Test
//    void testGetReviewsByProductId_EmptyList() {
//        // Arrange
//        Long productId = 2L;
//        when(reviewRepository.findByProductId(productId)).thenReturn(List.of());
//
//        // Act
//        List<Review> result = reviewService.getReviewsByProductId(productId);
//
//        // Assert
//        assertEquals(0, result.size());
//        verify(reviewRepository, times(1)).findByProductId(productId);
//    }
//}
package com.g02.handyShare.Review.Service;

import com.g02.handyShare.Review.Entity.Review;
import com.g02.handyShare.Review.Repository.ReviewRepository;
import com.g02.handyShare.Product.Entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void testGetReviewsByProductId() {
    //     // Arrange
    //     Long productId = 1L;
    //     Product mockProduct = new Product(
    //             productId,
    //             "Drill Machine",
    //             "Powerful drill machine for home use",
    //             "Tools",
    //             "drill.jpg",
    //             50.0,
    //             null,  // Assuming lender is not important for this test
    //             LocalDateTime.now(),
    //             true
    //     );

    //     List<Review> mockReviews = Arrays.asList(
    //             new Review(1L, "Good Product", "5", mockProduct),
    //             new Review(2L, "Could be better", "3", mockProduct)
    //     );


    //     when(reviewRepository.findByProductId(productId)).thenReturn(mockReviews);

    //     // Act
    //     List<Review> result = reviewService.getReviewsByProductId(productId);

    //     // Assert
    //     assertEquals(2, result.size());
    //     assertEquals("Good Product", result.get(0).getComment());
    //     assertEquals(productId, result.get(0).getProduct().getId());
    //     verify(reviewRepository, times(1)).findByProductId(productId);
    // }
    
    @Test
void testGetReviewsByProductId() {
    // Arrange
    Long productId = 1L;
    Product mockProduct = new Product(
            productId,
            "Drill Machine",
            "Powerful drill machine for home use",
            "Tools",
            "drill.jpg",
            50.0,
            null,  // Lender not relevant for this test
            LocalDateTime.now(),
            true
    );

    List<Review> mockReviews = Arrays.asList(
            new Review(1L, "john_doe", "Good Product", mockProduct),
            new Review(2L, "jane_smith", "Could be better", mockProduct)
    );

    when(reviewRepository.findByProductId(productId)).thenReturn(mockReviews);

    // Act
    List<Review> result = reviewService.getReviewsByProductId(productId);

    // Assert
    assertEquals(2, result.size());
    assertEquals("Good Product", result.get(0).getComment());
    assertEquals("john_doe", result.get(0).getUser());
    assertEquals(productId, result.get(0).getProduct().getId());
    verify(reviewRepository, times(1)).findByProductId(productId);
}


    @Test
    void testGetReviewsByProductId_EmptyList() {
        // Arrange
        Long productId = 2L;
        when(reviewRepository.findByProductId(productId)).thenReturn(List.of());

        // Act
        List<Review> result = reviewService.getReviewsByProductId(productId);

        // Assert
        assertEquals(0, result.size());
        verify(reviewRepository, times(1)).findByProductId(productId);
    }
}

