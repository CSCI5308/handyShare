package com.g02.handyShare.Category.Service;

import com.g02.handyShare.Category.Entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category createCategory(Category category);

    Optional<Category> getCategoryById(Long categoryId);

    List<Category> getAllCategories();

    Category updateCategory(Long categoryId, Category categoryDetails);

    void deleteCategory(Long categoryId);

    List<Category> getCategoryTree();
}

