package com.g02.handyShare.Product.Controller;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Service.CustomException;
import com.g02.handyShare.Product.Service.ProductService;
import com.g02.handyShare.User.Service.UserService;

import jakarta.validation.Valid;

import com.g02.handyShare.Config.Firebase.FirebaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final FirebaseService firebaseService;

    private static final String PRODUCT_NOT_FOUND = "Product not found";
    private static final String PRODUCT_DELETED_SUCCESS = "Product deleted Successfully!";
    private static final String PRODUCT_ID_NOT_FOUND = "Product ID does not exist!";
    private static final String ERROR_UPDATING_PRODUCT = "Error while updating the product.";
    private static final String STATUS_KEY = "status";

    @Autowired
    public ProductController(ProductService productService, UserService userService, FirebaseService firebaseService) {
        this.productService = productService;
        this.userService = userService;
        this.firebaseService = firebaseService;
    }

    @PostMapping("/user/add")
    public ResponseEntity<?> addProducts(@ModelAttribute @Valid Product product,
                                        @RequestParam("image") MultipartFile file) {
        ResponseEntity<?> savedProduct = productService.addProduct(product, file);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/user/product/{id}")
    public ResponseEntity<?> viewProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(PRODUCT_NOT_FOUND);
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/user/allProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/user/product/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        return isDeleted ? ResponseEntity.ok(PRODUCT_DELETED_SUCCESS)
                         : ResponseEntity.status(HttpStatus.NOT_FOUND).body(PRODUCT_ID_NOT_FOUND);
    }

    @GetMapping("/user/newly-added")
    public ResponseEntity<List<Product>> getNewlyAddedProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.getNewlyAddedProductsByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/user/listUserItems")
    public ResponseEntity<List<Product>> listProducts() {
        return ResponseEntity.ok(productService.listProductsForUser());
    }

    @PutMapping("/user/product/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @ModelAttribute @Valid Product updatedProduct,
                                           @RequestParam(value = "image", required = false) MultipartFile file) {
        try {
            return productService.updateProduct(id, updatedProduct, file);
        } catch (CustomException ce) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ce.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERROR_UPDATING_PRODUCT);
        }
    }

    @PutMapping("/user/product/changeAvailability/{id}")
    public ResponseEntity<?> changeAvailability(@PathVariable Long id, @RequestBody Map<String, Boolean> statusMap) {
        Boolean status = statusMap.get(STATUS_KEY);
        return productService.changeAvailability(id, status);
    }
}
