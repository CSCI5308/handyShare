package com.g02.handyShare.Review.Controller;

import java.io.IOException;
import java.util.List;

import com.g02.handyShare.Review.Dto.ReviewCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/review-create")
    public ResponseEntity<ReviewResponse> createReview(
            @ModelAttribute ReviewCreateDTO reviewCreateDTO) throws IOException {

        Review review = reviewService.createReview(reviewCreateDTO);
        ReviewResponse response = new ReviewResponse("Review created successfully", review);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}