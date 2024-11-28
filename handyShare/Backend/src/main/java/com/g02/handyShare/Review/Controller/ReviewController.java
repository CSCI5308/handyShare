package com.g02.handyShare.Review.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Review.Dto.ReviewResponse;
import com.g02.handyShare.Review.Dto.ReviewWithUserDTO;
import com.g02.handyShare.Review.Entity.Review; 
import com.g02.handyShare.Review.Service.ReviewService; 

@RestController
@RequestMapping("/api/v1/user")  
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService; 
    private FirebaseService firebaseService;
    
    @Autowired
    public void Controller(FirebaseService firebaseService){
        this.firebaseService=firebaseService;
    }

    // Endpoint to get reviews for a product
    @GetMapping("/review-product/{productId}")
    public List<ReviewWithUserDTO> getReviewsForProduct(@PathVariable Long productId) {
        return reviewService.getReviewsForProduct(productId);
    }

    // Endpoint to get reviews given by a user
    @GetMapping("/review-user/{user}")
    public List<Review> getReviewsForUser(@PathVariable Long user) {
        return reviewService.getReviewsForUser(user);  
    }

    // Endpoint to create new review
    @PostMapping("/review-create")
    public ResponseEntity<ReviewResponse> createReview(
            @RequestParam("userId") Long userId,
            @RequestParam("productId") Long productId,
            @RequestParam("reviewText") String reviewText,
            @RequestParam("rating") int rating,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        
        Review review = reviewService.createReview(userId, productId, reviewText, rating, image);
    
        ReviewResponse response = new ReviewResponse("Review created successfully", review);
    
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
}