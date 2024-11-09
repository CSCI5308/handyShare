package com.g02.handyShare.Category.Service;

import com.g02.handyShare.Category.DTO.SubCategoryDTO;
import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Category.Repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_ShouldSaveAndReturnCategory() {
        // Arrange
        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category savedCategory = categoryService.createCategory(category);

        // Assert
        assertNotNull(savedCategory);
        assertEquals("Electronics", savedCategory.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void getCategoryById_ShouldReturnCategoryWithSubCategories_WhenCategoryExists() {
        // Arrange
        Long categoryId = 1L;
        Category parentCategory = new Category();
        parentCategory.setCategoryId(categoryId);
        parentCategory.setName("Electronics");

        Category subCategory = new Category();
        subCategory.setCategoryId(2L);
        subCategory.setName("Mobile Phones");
        subCategory.setParentCategory(parentCategory);

        List<Category> allCategories = Arrays.asList(parentCategory, subCategory);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.findAll()).thenReturn(allCategories);

        // Act
        Optional<Category> result = categoryService.getCategoryById(categoryId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getName());
        List<SubCategoryDTO> subCategories = result.get().getSubCategories();
        assertEquals(1, subCategories.size());
        assertEquals("Mobile Phones", subCategories.get(0).getName());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_ShouldReturnEmpty_WhenCategoryDoesNotExist() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act
        Optional<Category> result = categoryService.getCategoryById(categoryId);

        // Assert
        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).findAll();
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setName("Electronics");
        Category category2 = new Category();
        category2.setName("Furniture");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // Act
        List<Category> categories = categoryService.getAllCategories();

        // Assert
        assertEquals(2, categories.size());
        assertEquals("Electronics", categories.get(0).getName());
        assertEquals("Furniture", categories.get(1).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void updateCategory_ShouldUpdateAndReturnCategory_WhenCategoryExists() {
        // Arrange
        Long categoryId = 1L;
        Category existingCategory = new Category();
        existingCategory.setCategoryId(categoryId);
        existingCategory.setName("Electronics");

        Category updatedDetails = new Category();
        updatedDetails.setName("Updated Electronics");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        // Act
        Category updatedCategory = categoryService.updateCategory(categoryId, updatedDetails);

        // Assert
        assertNotNull(updatedCategory);
        assertEquals("Updated Electronics", updatedCategory.getName());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    void updateCategory_ShouldThrowException_WhenCategoryDoesNotExist() {
        // Arrange
        Long categoryId = 1L;
        Category updatedDetails = new Category();
        updatedDetails.setName("Updated Electronics");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.updateCategory(categoryId, updatedDetails));
        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void deleteCategory_ShouldDeleteCategory_WhenCategoryExists() {
        // Arrange
        Long categoryId = 1L;
        Category category = new Category();
        category.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act
        categoryService.deleteCategory(categoryId);

        // Assert
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void deleteCategory_ShouldThrowException_WhenCategoryDoesNotExist() {
        // Arrange
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(categoryId));
        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void getCategoryTree_ShouldReturnCategoryTreeWithSubCategories() {
        // Arrange
        Category parentCategory = new Category();
        parentCategory.setCategoryId(1L);
        parentCategory.setName("Electronics");

        Category subCategory = new Category();
        subCategory.setCategoryId(2L);
        subCategory.setName("Mobile Phones");
        subCategory.setParentCategory(parentCategory);

        List<Category> allCategories = Arrays.asList(parentCategory, subCategory);

        when(categoryRepository.findAll()).thenReturn(allCategories);

        // Act
        List<Category> categoryTree = categoryService.getCategoryTree();

        // Assert
        assertEquals(1, categoryTree.size());
        assertEquals("Electronics", categoryTree.get(0).getName());
        List<SubCategoryDTO> subCategories = categoryTree.get(0).getSubCategories();
        assertEquals(1, subCategories.size());
        assertEquals("Mobile Phones", subCategories.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }
}
