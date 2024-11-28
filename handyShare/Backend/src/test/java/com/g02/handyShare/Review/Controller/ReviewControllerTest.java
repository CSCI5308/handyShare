package com.g02.handyShare.Review.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.g02.handyShare.Review.Dto.ReviewResponse;
import com.g02.handyShare.Review.Dto.ReviewWithUserDTO;
import com.g02.handyShare.Review.Entity.Review;
import com.g02.handyShare.Review.Service.ReviewService;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;

class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private FirebaseService firebaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReviewsForProduct_Success() {
        Long productId = 1L;
        List<ReviewWithUserDTO> reviews = new ArrayList<>();
        reviews.add(new ReviewWithUserDTO(1L, "User1", productId, "Great product!", 5, null));

        when(reviewService.getReviewsForProduct(productId)).thenReturn(reviews);

        List<ReviewWithUserDTO> response = reviewController.getReviewsForProduct(productId);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Great product!", response.get(0).getReviewText());
    }

    @Test
    public void testGetReviewsForProduct_NotFound() {
        Long productId = 1L;
        when(reviewService.getReviewsForProduct(productId)).thenReturn(new ArrayList<>());

        List<ReviewWithUserDTO> response = reviewController.getReviewsForProduct(productId);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    public void testGetReviewsForUser_Success() {
        Long userId = 1L;
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(userId, 1L, "Nice product!", 4, null));

        when(reviewService.getReviewsForUser(userId)).thenReturn(reviews);

        List<Review> response = reviewController.getReviewsForUser(userId);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Nice product!", response.get(0).getReviewText());
    }

    @Test
    public void testGetReviewsForUser_NotFound() {
        Long userId = 1L;
        when(reviewService.getReviewsForUser(userId)).thenReturn(new ArrayList<>());

        List<Review> response = reviewController.getReviewsForUser(userId);

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    public void testCreateReview_Success() throws Exception {
        Long userId = 1L;
        Long productId = 1L;
        String reviewText = "Excellent!";
        int rating = 5;
        MultipartFile image = null;

        Review review = new Review(userId, productId, reviewText, rating, null);
        when(reviewService.createReview(userId, productId, reviewText, rating, image)).thenReturn(review);

        ResponseEntity<ReviewResponse> response = reviewController.createReview(userId, productId, reviewText, rating, image);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Review created successfully", response.getBody().getMessage());
        assertEquals(review, response.getBody().getReview());
    }

    @Test
    public void testCreateReview_Failure() throws Exception {
        Long userId = 1L;
        Long productId = 1L;
        String reviewText = "Bad review";
        int rating = 1;
        MultipartFile image = null;

        when(reviewService.createReview(userId, productId, reviewText, rating, image)).thenThrow(new RuntimeException("Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            reviewController.createReview(userId, productId, reviewText, rating, image);
        });

        assertEquals("Error", exception.getMessage());
    }
}