package com.g02.handyShare.Product.Controller;

import com.g02.handyShare.Product.Model.Product;
import com.g02.handyShare.Product.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product){
        productService.addProduct(product);
        Map<String, String> response=new HashMap<>();
        response.put("message", "Product added Successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product){
       Product updatedProduct=productService.updateProduct(id, product);
       return new ResponseEntity<>(updatedProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        Map<String, String> response=new HashMap<>();
        response.put("message", "Product removed Successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
