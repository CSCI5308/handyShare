package com.g02.handyShare.Review.Dto;

import org.springframework.web.multipart.MultipartFile;

public class ReviewCreateDTO {
    private Long userId;
    private Long productId;
    private String reviewText;
    private int rating;
    private MultipartFile image;

    // Constructors
    public ReviewCreateDTO() {}

    public ReviewCreateDTO(Long userId, Long productId, String reviewText, int rating, MultipartFile image) {
        this.userId = userId;
        this.productId = productId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.image = image;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
