package com.g02.handyShare.Product.Controller;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.Product.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true" )

public class ProductController {
    @Autowired
    private ProductService productService;

     @Autowired
    private ProductRepository productRepository;

      private FirebaseService firebaseService;

    @Autowired
    public void Controller(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }


    @PostMapping("/user/add")
    public ResponseEntity<?> addProducts(@ModelAttribute Product product,
                                         @RequestParam("image") MultipartFile file) {

                                            
    
        // Upload the image file and store its URL in the product's image data
        ResponseEntity<?> savedProduct = productService.addProduct(product, file);
    
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }
    



    @GetMapping("user/product/{id}")
    public Product viewProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/user/allProducts")
    public List<Product> getAllCategories() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/user/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted){
            return ResponseEntity.ok("Product deleted Successfully!");
        } else {
            return ResponseEntity.status(404).body("Product ID does not exist!");
        }
    }

 

    @GetMapping("/user/newly-added")
    public ResponseEntity<List<Product>> getNewlyAddedProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.getNewlyAddedProductsByCategory(category);
        return ResponseEntity.ok().body(products);
    }

    //    @PutMapping("/update/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product){
//        Product updatedProduct=productService.updateProduct(id, product);
//        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//    }
}
