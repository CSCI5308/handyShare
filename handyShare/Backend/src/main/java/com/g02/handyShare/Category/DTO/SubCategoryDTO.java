package com.g02.handyShare.Category.DTO;

public class SubCategoryDTO {
    private Long categoryId;
    private String name;

    // Constructor
    public SubCategoryDTO(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

