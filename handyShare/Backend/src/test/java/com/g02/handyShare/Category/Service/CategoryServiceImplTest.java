package com.g02.handyShare.Category.Service;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Category.Repository.CategoryRepository;
import com.g02.handyShare.Category.DTO.SubCategoryDTO;
import com.g02.handyShare.Category.Service.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category parentCategory;
    private Category subCategory;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a parent category
        parentCategory = new Category();
        parentCategory.setCategoryId(1L);
        parentCategory.setName("Parent Category");

        // Create a subcategory
        subCategory = new Category();
        subCategory.setCategoryId(2L);
        subCategory.setName("Sub Category");
        subCategory.setParentCategory(parentCategory);
    }

    @Test
    void testCreateCategory() {
        // Arrange
        when(categoryRepository.save(parentCategory)).thenReturn(parentCategory);

        // Act
        Category result = categoryService.createCategory(parentCategory);

        // Assert
        assertNotNull(result);
        assertEquals("Parent Category", result.getName());
        verify(categoryRepository, times(1)).save(parentCategory);
    }

    @Test
    void testGetCategoryById() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        List<Category> allCategories = List.of(parentCategory, subCategory);
        when(categoryRepository.findAll()).thenReturn(allCategories);

        // Act
        Optional<Category> result = categoryService.getCategoryById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Parent Category", result.get().getName());
        assertEquals(1, result.get().getSubCategories().size());  // Should have 1 subcategory
        assertEquals("Sub Category", result.get().getSubCategories().get(0).getName());
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        List<Category> allCategories = List.of(parentCategory, subCategory);
        when(categoryRepository.findAll()).thenReturn(allCategories);

        // Act
        List<Category> result = categoryService.getAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Parent Category", result.get(0).getName());
    }

    @Test
    void testUpdateCategory() {
        // Arrange
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Category");
        updatedCategory.setDescription("Updated Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(parentCategory)).thenReturn(parentCategory);

        // Act
        Category result = categoryService.updateCategory(1L, updatedCategory);

        // Assert
        assertEquals("Updated Category", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testDeleteCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(parentCategory));

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).delete(parentCategory);
    }

    @Test
    void testGetCategoryTree() {
        // Arrange
        List<Category> allCategories = List.of(parentCategory, subCategory);
        when(categoryRepository.findAll()).thenReturn(allCategories);

        // Act
        List<Category> result = categoryService.getCategoryTree();

        // Assert
        assertEquals(1, result.size()); // Only the parent category should be in the tree
        assertEquals("Parent Category", result.get(0).getName());
        assertEquals(1, result.get(0).getSubCategories().size()); // Should have 1 subcategory
    }

    @Test
    void testGetSubCategoryDTOs() throws Exception {
        // Prepare the data
        List<Category> allCategories = List.of(parentCategory, subCategory);

        // Get the private method using reflection
        Method method = CategoryServiceImpl.class.getDeclaredMethod("getSubCategoryDTOs", Long.class, List.class);
        method.setAccessible(true);

        // Invoke the method
        List<SubCategoryDTO> result = (List<SubCategoryDTO>) method.invoke(categoryService, 1L, allCategories);

        // Assert the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sub Category", result.get(0).getName());
    }
}
