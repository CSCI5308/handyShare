package com.g02.handyShare.Category.Controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import com.g02.handyShare.Category.Controller.CategoryController;
import com.g02.handyShare.Category.Service.CategoryService;
import com.g02.handyShare.Category.Entity.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category validCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validCategory = new Category();
        validCategory.setName("Electronics");
        validCategory.setDescription("All kinds of electronics");
        validCategory.setIsActive(true);
    }

    @Test
    void createCategory_Success() {
        when(categoryService.createCategory(any(Category.class))).thenReturn(validCategory);

        ResponseEntity<Category> response = categoryController.createCategory(Map.of("name", "Electronics", "description", "All kinds of electronics"));

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Electronics", response.getBody().getName());
    }

    @Test
    void createCategory_Failure_NameEmpty() {
        ResponseEntity<Category> response = categoryController.createCategory(Map.of("name", "", "description", "All kinds of electronics"));

        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getCategoryById_Success() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(validCategory));

        ResponseEntity<?> response = categoryController.getCategoryById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Electronics", ((Category) response.getBody()).getName());
    }

    @Test
    void getCategoryById_NotFound() {
        when(categoryService.getCategoryById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = categoryController.getCategoryById(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Category not found", ((Map<String, String>) response.getBody()).get("message"));
    }

    @Test
    void getAllCategories_Success() {
        List<Category> categories = List.of(validCategory);
        when(categoryService.getAllCategories()).thenReturn(categories);

        List<Category> response = categoryController.getAllCategories();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Electronics", response.get(0).getName());
    }

    @Test
    void updateCategory_Success() {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Electronics");
        updatedCategory.setDescription("Updated description");
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(validCategory));
        when(categoryService.updateCategory(1L, updatedCategory)).thenReturn(updatedCategory);

        ResponseEntity<Category> response = categoryController.updateCategory(1L, updatedCategory);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Electronics", response.getBody().getName());
    }

    @Test
    void updateCategory_NotFound() {
        Category updatedCategory = new Category();
        updatedCategory.setName("Updated Electronics");
        updatedCategory.setDescription("Updated description");

        when(categoryService.getCategoryById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Category> response = categoryController.updateCategory(99L, updatedCategory);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteCategory_Success() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(validCategory));
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<?> response = categoryController.deleteCategory(1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteCategory_NotFound() {
        when(categoryService.getCategoryById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = categoryController.deleteCategory(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Category not found", response.getBody());
    }

}
