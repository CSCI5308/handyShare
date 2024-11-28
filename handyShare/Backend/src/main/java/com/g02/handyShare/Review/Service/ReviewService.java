package com.g02.handyShare.Review.Service;

import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Review.Dto.ReviewWithUserDTO;
import com.g02.handyShare.Review.Entity.Review;  
import com.g02.handyShare.Review.Repository.ReviewRepository;
import com.g02.handyShare.User.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;  

    @Autowired
    private UserRepository userRepository;

    private FirebaseService firebaseService;

    @Autowired
    public void Controller(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

        public List<ReviewWithUserDTO> getReviewsForProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        
        return reviews.stream().map(review -> {
            String username = userRepository.findById(review.getUserId())
                                .map(user -> user.getName())
                                .orElse("Anonymous"); 
            return new ReviewWithUserDTO(
                review.getId(),
                username,
                review.getProductId(),
                review.getReviewText(),
                review.getRating(),
                review.getImage()
            );
        }).collect(Collectors.toList());
    }

    public List<Review> getReviewsForUser(Long userId) {
        return reviewRepository.findByUserId(userId);  
    }

    public Review createReview(Long userId, Long productId, String reviewText, int rating, MultipartFile image) throws IOException {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = firebaseService.uploadFile(image, "/reviews");
            } catch (IOException e) {
                throw new IOException("Failed to upload image", e);
            }
        }
        Review review = new Review(userId, productId, reviewText, rating, imageUrl);  
        return reviewRepository.save(review); 
    }
}
