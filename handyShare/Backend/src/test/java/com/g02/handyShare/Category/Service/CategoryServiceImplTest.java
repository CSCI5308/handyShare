package com.g02.handyShare.Category.Service;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Category.Repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setCategoryId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");
        category.setIsActive(true);
        category.setSortOrder(1);
    }

    @Test
    public void testCreateCategory() {
        // Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        Category createdCategory = categoryService.createCategory(category);

        // Assert
        assertNotNull(createdCategory);
        assertEquals("Test Category", createdCategory.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testGetCategoryById_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        Optional<Category> fetchedCategory = categoryService.getCategoryById(1L);

        // Assert
        assertTrue(fetchedCategory.isPresent());
        assertEquals("Test Category", fetchedCategory.get().getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCategoryById_NotFound() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Category> fetchedCategory = categoryService.getCategoryById(1L);

        // Assert
        assertFalse(fetchedCategory.isPresent());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        // Act
        List<Category> categories = categoryService.getAllCategories();

        // Assert
        assertNotNull(categories);
        assertEquals(1, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateCategory_Success() {
        // Arrange
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");
        updatedCategory.setDescription("Updated Description");
        updatedCategory.setIsActive(false);
        updatedCategory.setSortOrder(2);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        // Act
        Category result = categoryService.updateCategory(1L, updatedCategory);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Category", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertFalse(result.getIsActive());
        assertEquals(2, result.getSortOrder());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategory_NotFound() {
        // Arrange
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.updateCategory(1L, updatedCategory);
        });

        // Assert
        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteCategory_Success() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).delete(any(Category.class));
    }

    @Test
    public void testDeleteCategory_NotFound() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        // Assert
        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository, times(1)).findById(1L);
    }
}
