package com.g02.handyShare.Category.Service;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Category.Repository.CategoryRepository;
import com.g02.handyShare.Category.DTO.SubCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getCategoryById(Long categoryId) {

        Optional<Category> category = categoryRepository.findById(categoryId);

        // Fetch and map subcategories
        if (category.isPresent()) {
            List<Category> allCategories = categoryRepository.findAll();
            List<SubCategoryDTO> subCategoryDTOs = allCategories.stream()
                    .filter(subCategory -> subCategory.getParentCategory() != null &&
                            subCategory.getParentCategory().getCategoryId().equals(categoryId))
                    .map(subCategory -> new SubCategoryDTO(subCategory.getCategoryId(), subCategory.getName()))
                    .toList();

            category.get().setSubCategories(subCategoryDTOs);
        }

        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long categoryId, Category categoryDetails) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Update fields
        if (categoryDetails.getName() != null) {
            category.setName(categoryDetails.getName());
        }
        if (categoryDetails.getDescription() != null) {
            category.setDescription(categoryDetails.getDescription());
        }
        if (categoryDetails.getIsActive() != null) {
            category.setIsActive(categoryDetails.getIsActive());
        }
        if (categoryDetails.getParentCategory() != null) {
            category.setParentCategory(categoryDetails.getParentCategory());
        }
        if (categoryDetails.getSortOrder() != null) {
            category.setSortOrder(categoryDetails.getSortOrder());
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .filter(category -> category.getParentCategory() == null)
                .peek(parent -> parent.setSubCategories(getSubCategoryDTOs(parent.getCategoryId(), allCategories)))
                .toList();
    }

    private List<SubCategoryDTO> getSubCategoryDTOs(Long parentId, List<Category> allCategories) {
        return allCategories.stream()
                .filter(category -> category.getParentCategory() != null &&
                        category.getParentCategory().getCategoryId().equals(parentId))
                .map(subCategory -> new SubCategoryDTO(subCategory.getCategoryId(), subCategory.getName()))
                .toList();
    }
}
