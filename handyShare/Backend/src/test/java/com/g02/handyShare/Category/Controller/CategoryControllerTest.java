package com.g02.handyShare.Category.Controller;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Category.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_ShouldReturnCreatedStatus_WhenCategoryIsCreatedSuccessfully() {
        // Arrange
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronic items");

        Map<String, Object> categoryData = Map.of(
                "name", "Electronics",
                "description", "Electronic items",
                "isActive", true
        );

        Category savedCategory = new Category();
        savedCategory.setCategoryId(1L);
        savedCategory.setName("Electronics");

        doReturn(savedCategory).when(categoryService).createCategory(any(Category.class));

        // Act
        ResponseEntity<Category> response = categoryController.createCategory(categoryData);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/api/v1/categories/1"), response.getHeaders().getLocation());
        assertEquals("Electronics", response.getBody().getName());
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void getCategoryById_ShouldReturnCategory_WhenCategoryExists() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Electronics");

        doReturn(Optional.of(category)).when(categoryService).getCategoryById(1L);

        // Act
        ResponseEntity<?> response = categoryController.getCategoryById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Electronics", ((Category) response.getBody()).getName());
        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void getCategoryById_ShouldReturnNotFoundMessage_WhenCategoryDoesNotExist() {
        // Arrange
        doReturn(Optional.empty()).when(categoryService).getCategoryById(1L);

        // Act
        ResponseEntity<?> response = categoryController.getCategoryById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Map.of("message", "Category not found"), response.getBody());
        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setName("Electronics");
        Category category2 = new Category();
        category2.setName("Furniture");

        List<Category> categories = Arrays.asList(category1, category2);
        doReturn(categories).when(categoryService).getAllCategories();

        // Act
        List<Category> result = categoryController.getAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Furniture", result.get(1).getName());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void updateCategory_ShouldReturnUpdatedCategory_WhenCategoryExists() {
        // Arrange
        Category categoryDetails = new Category();
        categoryDetails.setName("Updated Electronics");

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setName("Updated Electronics");

        doReturn(updatedCategory).when(categoryService).updateCategory(eq(1L), any(Category.class));

        // Act
        ResponseEntity<Category> response = categoryController.updateCategory(1L, categoryDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Electronics", response.getBody().getName());
        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void deleteCategory_ShouldReturnOkStatus_WhenCategoryIsDeleted() {
        // Act
        ResponseEntity<?> response = categoryController.deleteCategory(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void getCategoryTree_ShouldReturnCategoryTree() {
        // Arrange
        Category category1 = new Category();
        category1.setName("Electronics");

        List<Category> categoryTree = Arrays.asList(category1);
        doReturn(categoryTree).when(categoryService).getCategoryTree();

        // Act
        List<Category> result = categoryController.getCategoryTree();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
        verify(categoryService, times(1)).getCategoryTree();
    }
}
