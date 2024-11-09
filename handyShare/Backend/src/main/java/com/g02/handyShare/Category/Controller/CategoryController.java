package com.g02.handyShare.Category.Controller;

import com.g02.handyShare.Category.DTO.SubCategoryDTO;
import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Category.Service.CategoryService;
import com.g02.handyShare.Category.Service.CategoryServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true" )

// @CrossOrigin(origins = "http://172.17.0.99:3000", 
// allowedHeaders = {"Authorization", "Content-Type"}, 
// methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class CategoryController {

  @Autowired
    private CategoryService categoryService ;

    @PostMapping("/all/create")
    public ResponseEntity<Category> createCategory(@RequestBody Map<String, Object> categoryData) {
        Category category = new Category();
        category.setName((String) categoryData.get("name"));
        category.setDescription((String) categoryData.get("description"));

        if (categoryData.containsKey("parent_category_id")) {
            Long parentCategoryId = Long.valueOf(categoryData.get("parent_category_id").toString());
            category.setParentCategory(categoryService.getCategoryById(parentCategoryId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found")));
        }

        // Set the isActive status from the incoming data, defaulting to true if not provided
        Boolean isActive = (Boolean) categoryData.getOrDefault("isActive", true);
        category.setIsActive(isActive);

        Category savedCategory = categoryService.createCategory(category);
        return ResponseEntity
                .created(URI.create("/api/v1/categories/" + savedCategory.getCategoryId()))
                .body(savedCategory);
    }

    @GetMapping("/user/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable(value = "id") Long categoryId) {
        Optional<Category> category = categoryService.getCategoryById(categoryId);

        // If category is not found, return custom message with 404 status
        if (!category.isPresent()) {
            Map<String, String> response = Map.of("message", "Category not found");
            return ResponseEntity.status(404).body(response);
        }

        // If found, return the category
        return ResponseEntity.ok().body(category.get());
    }


    @GetMapping("/user/allCategories")
    public List<Category> getAllCategories() {
        System.out.println("Fetching all categories");
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("Found " + categories.size() + " categories");
        return categories;
    }

    @PutMapping("/all/category/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable(value = "id") Long categoryId,
                                                   @RequestBody Category categoryDetails) {
        Category updatedCategory = categoryService.updateCategory(categoryId, categoryDetails);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/all/category/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable(value = "id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tree")
    public List<Category> getCategoryTree() {
        return categoryService.getCategoryTree();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public void handleOptions() {
        // this will allow the preflight request to pass
    }
}
