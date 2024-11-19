//
//package com.g02.handyShare.Category.Controller;
//
//import com.g02.handyShare.Category.Entity.Category;
//import com.g02.handyShare.Category.DTO.SubCategoryDTO;
//import com.g02.handyShare.Category.Service.CategoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest  // Use Spring Boot test context
//@AutoConfigureMockMvc  // Automatically configure MockMvc for the tests
//class CategoryControllerTest {
//
//    private MockMvc mockMvc;
//
//    @MockBean  // This mocks the CategoryService bean
//    private CategoryService categoryService;
//
//    @InjectMocks  // Injects the mock CategoryService into the controller
//    private CategoryController categoryController;
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
//                .build();  // Ensure MockMvc is correctly initialized
//    }
//
//    @Test
//    void testCreateCategory_Success() throws Exception {
//        // Arrange
//        SubCategoryDTO subCategory = new SubCategoryDTO(1L, "Mobile Phones");
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setName("Electronics");
//        category.setDescription("Electronic devices");
//        category.setIsActive(true);
//        category.setSubCategories(Collections.singletonList(subCategory));
//
//        when(categoryService.createCategory(any(Category.class))).thenReturn(category);
//
//        // Act & Assert
//        mockMvc.perform(post("/api/v1/all/create")
//                        .contentType("application/json")
//                        .content("{\"name\": \"Electronics\", \"description\": \"Electronic devices\", \"isActive\": true}"))
//                .andExpect(status().isCreated())
//                .andExpect(header().string("Location", "/api/v1/categories/1"))
//                .andExpect(jsonPath("$.categoryId").value(1))
//                .andExpect(jsonPath("$.name").value("Electronics"))
//                .andExpect(jsonPath("$.description").value("Electronic devices"))
//                .andExpect(jsonPath("$.subCategories[0].categoryId").value(1))
//                .andExpect(jsonPath("$.subCategories[0].name").value("Mobile Phones"));
//
//        verify(categoryService, times(1)).createCategory(any(Category.class));
//    }
//
//    @Test
//    void testCreateCategory_Failure() throws Exception {
//        // Arrange
//        when(categoryService.createCategory(any(Category.class))).thenThrow(new RuntimeException("Error"));
//
//        // Act & Assert
//        mockMvc.perform(post("/api/v1/all/create")
//                        .contentType("application/json")
//                        .content("{\"name\": \"Electronics\", \"description\": \"Electronic devices\", \"isActive\": true}"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("Error"));
//
//        verify(categoryService, times(1)).createCategory(any(Category.class));
//    }
//
//    @Test
//    void testGetCategoryById_Success() throws Exception {
//        // Arrange
//        SubCategoryDTO subCategory = new SubCategoryDTO(1L, "Mobile Phones");
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setName("Electronics");
//        category.setDescription("Electronic devices");
//        category.setSubCategories(Collections.singletonList(subCategory));
//
//        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/user/category/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").value(1))
//                .andExpect(jsonPath("$.name").value("Electronics"))
//                .andExpect(jsonPath("$.description").value("Electronic devices"))
//                .andExpect(jsonPath("$.subCategories[0].categoryId").value(1))
//                .andExpect(jsonPath("$.subCategories[0].name").value("Mobile Phones"));
//
//        verify(categoryService, times(1)).getCategoryById(1L);
//    }
//
//    @Test
//    void testGetCategoryById_NotFound() throws Exception {
//        // Arrange
//        when(categoryService.getCategoryById(anyLong())).thenReturn(Optional.empty());
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/user/category/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Category not found"));
//
//        verify(categoryService, times(1)).getCategoryById(1L);
//    }
//
//    @Test
//    void testGetAllCategories() throws Exception {
//        // Arrange
//        SubCategoryDTO subCategory = new SubCategoryDTO(1L, "Mobile Phones");
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setName("Electronics");
//        category.setDescription("Electronic devices");
//        category.setSubCategories(Collections.singletonList(subCategory));
//
//        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(category));
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/user/allCategories"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].categoryId").value(1))
//                .andExpect(jsonPath("$[0].name").value("Electronics"))
//                .andExpect(jsonPath("$[0].description").value("Electronic devices"))
//                .andExpect(jsonPath("$[0].subCategories[0].categoryId").value(1))
//                .andExpect(jsonPath("$[0].subCategories[0].name").value("Mobile Phones"));
//
//        verify(categoryService, times(1)).getAllCategories();
//    }
//
//    @Test
//    void testUpdateCategory_Success() throws Exception {
//        // Arrange
//        SubCategoryDTO subCategory = new SubCategoryDTO(1L, "Mobile Phones");
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setName("Electronics");
//        category.setDescription("Electronic devices");
//        category.setSubCategories(Collections.singletonList(subCategory));
//
//        when(categoryService.updateCategory(anyLong(), any(Category.class))).thenReturn(category);
//
//        // Act & Assert
//        mockMvc.perform(put("/api/v1/all/category/1")
//                        .contentType("application/json")
//                        .content("{\"name\": \"Electronics Updated\", \"description\": \"Updated devices\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.categoryId").value(1))
//                .andExpect(jsonPath("$.name").value("Electronics Updated"))
//                .andExpect(jsonPath("$.description").value("Updated devices"))
//                .andExpect(jsonPath("$.subCategories[0].categoryId").value(1))
//                .andExpect(jsonPath("$.subCategories[0].name").value("Mobile Phones"));
//
//        verify(categoryService, times(1)).updateCategory(anyLong(), any(Category.class));
//    }
//
//    @Test
//    void testDeleteCategory_Success() throws Exception {
//        // Arrange
//        doNothing().when(categoryService).deleteCategory(anyLong());
//
//        // Act & Assert
//        mockMvc.perform(delete("/api/v1/all/category/delete/1"))
//                .andExpect(status().isOk());
//
//        verify(categoryService, times(1)).deleteCategory(1L);
//    }
//
//    @Test
//    void testGetCategoryTree() throws Exception {
//        // Arrange
//        SubCategoryDTO subCategory = new SubCategoryDTO(1L, "Mobile Phones");
//        Category category = new Category();
//        category.setCategoryId(1L);
//        category.setName("Electronics");
//        category.setDescription("Electronic devices");
//        category.setSubCategories(Collections.singletonList(subCategory));
//
//        when(categoryService.getCategoryTree()).thenReturn(Collections.singletonList(category));
//
//        // Act & Assert
//        mockMvc.perform(get("/api/v1/tree"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].categoryId").value(1))
//                .andExpect(jsonPath("$[0].name").value("Electronics"))
//                .andExpect(jsonPath("$[0].description").value("Electronic devices"))
//                .andExpect(jsonPath("$[0].subCategories[0].categoryId").value(1))
//                .andExpect(jsonPath("$[0].subCategories[0].name").value("Mobile Phones"));
//
//        verify(categoryService, times(1)).getCategoryTree();
//    }
//
//    @Test
//    void testOptionsRequest() throws Exception {
//        // Act & Assert
//        mockMvc.perform(options("/api/v1/all/create"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(options("/api/v1/user/category/1"))
//                .andExpect(status().isOk());
//    }
//}
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category testCategory;
    private Map<String, Object> categoryData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test category
        testCategory = new Category();
        testCategory.setCategoryId(1L);
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");
        testCategory.setIsActive(true);

        // Setup category data for creation
        categoryData = new HashMap<>();
        categoryData.put("name", "Test Category");
        categoryData.put("description", "Test Description");
        categoryData.put("isActive", true);
    }

    @Test
    void createCategory_Success() {
        when(categoryService.createCategory(any(Category.class))).thenReturn(testCategory);

        ResponseEntity<Category> response = categoryController.createCategory(categoryData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Category", response.getBody().getName());
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void createCategory_WithParentCategory_Success() {
        Category parentCategory = new Category();
        parentCategory.setCategoryId(2L);

        categoryData.put("parent_category_id", 2L);

        when(categoryService.getCategoryById(2L)).thenReturn(Optional.of(parentCategory));
        when(categoryService.createCategory(any(Category.class))).thenReturn(testCategory);

        ResponseEntity<Category> response = categoryController.createCategory(categoryData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(categoryService, times(1)).getCategoryById(2L);
        verify(categoryService, times(1)).createCategory(any(Category.class));
    }

    @Test
    void getCategoryById_Success() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(testCategory));

        ResponseEntity<?> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testCategory, response.getBody());
    }

    @Test
    void getCategoryById_NotFound() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        assertEquals("Category not found", ((Map<?, ?>) response.getBody()).get("message"));
    }

    @Test
    void getAllCategories_Success() {
        List<Category> categories = Arrays.asList(testCategory);
        when(categoryService.getAllCategories()).thenReturn(categories);

        List<Category> response = categoryController.getAllCategories();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testCategory, response.get(0));
    }

    @Test
    void updateCategory_Success() {
        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setName("Updated Category");

        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(updatedCategory);

        ResponseEntity<Category> response = categoryController.updateCategory(1L, updatedCategory);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Category", response.getBody().getName());
    }

    @Test
    void deleteCategory_Success() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<?> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void getCategoryTree_Success() {
        List<Category> categoryTree = Arrays.asList(testCategory);
        when(categoryService.getCategoryTree()).thenReturn(categoryTree);

        List<Category> response = categoryController.getCategoryTree();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(testCategory, response.get(0));
    }
}