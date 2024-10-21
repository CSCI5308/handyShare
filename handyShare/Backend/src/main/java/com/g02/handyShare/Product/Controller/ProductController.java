package com.g02.handyShare.Product.Controller;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/all")

@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProducts(@RequestBody  List<Product> products) {
        List<Product> savedProducts = productService.addProduct(products);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Products added successfully.");
        response.put("products", savedProducts);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product){
//        Product updatedProduct=productService.updateProduct(id, product);
//        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public Product viewProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/allProducts")
    public List<Product> getAllCategories() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        boolean isDeleted=productService.deleteProduct(id);
        if (isDeleted){
            return ResponseEntity.ok("Product deleted Successfully!");
        }else{
            return ResponseEntity.status(404).body("Product ID does not exist!");
        }
    }

