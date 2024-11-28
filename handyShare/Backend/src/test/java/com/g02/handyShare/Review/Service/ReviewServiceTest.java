package com.g02.handyShare.Review.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.g02.handyShare.Review.Dto.ReviewWithUserDTO;
import com.g02.handyShare.Review.Entity.Review;
import com.g02.handyShare.Review.Repository.ReviewRepository;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.User.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FirebaseService firebaseService;

    @Mock
    private MultipartFile image;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReviewsForProduct() {
        Long productId = 1L;
        List<Review> reviews = List.of(new Review(1L, productId, "Great product!", 5, null));
        when(reviewRepository.findByProductId(productId)).thenReturn(reviews);

        List<ReviewWithUserDTO> result = reviewService.getReviewsForProduct(productId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Great product!", result.get(0).getReviewText());
    }

    @Test
    public void testCreateReviewWithImage() throws IOException {
        Long userId = 1L;
        Long productId = 1L;
        String reviewText = "Excellent!";
        int rating = 5;
        String imageUrl = "image_url";

        when(firebaseService.uploadFile(image, "/reviews")).thenReturn(imageUrl);
        when(reviewRepository.save(any(Review.class))).thenReturn(new Review(userId, productId, reviewText, rating, imageUrl));

        Review review = reviewService.createReview(userId, productId, reviewText, rating, image);

        assertNotNull(review);
        assertEquals(imageUrl, review.getImage());
    }

    @Test
    public void testCreateReviewWithoutImage() throws IOException {
        Long userId = 1L;
        Long productId = 1L;
        String reviewText = "Great!";
        int rating = 4;

        when(reviewRepository.save(any(Review.class))).thenReturn(new Review(userId, productId, reviewText, rating, null));

        Review review = reviewService.createReview(userId, productId, reviewText, rating, null);

        assertNotNull(review);
        assertNull(review.getImage());
    }
}