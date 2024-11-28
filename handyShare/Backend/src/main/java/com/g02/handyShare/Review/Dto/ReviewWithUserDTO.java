package com.g02.handyShare.Review.Dto;

public class ReviewWithUserDTO {
    private Long id;
    private String username;  
    private Long productId;
    private String reviewText;
    private int rating;
    private String image;  

    public ReviewWithUserDTO(Long id, String username, Long productId, String reviewText, int rating, String image) {
        this.id = id;
        this.username = username;
        this.productId = productId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.image = image;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}