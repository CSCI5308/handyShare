package com.g02.handyShare.Review.Dto;

import com.g02.handyShare.Review.Entity.Review;

public class ReviewResponse {
    private String message;
    private Review review;

    public ReviewResponse(String message, Review review) {
        this.message = message;
        this.review = review;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}