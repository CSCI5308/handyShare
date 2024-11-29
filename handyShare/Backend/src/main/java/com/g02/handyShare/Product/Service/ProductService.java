package com.g02.handyShare.Product.Service;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private FirebaseService firebaseService;

    @Autowired
    public void Controller(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> addProduct(Product product, MultipartFile file) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication.getName());
            System.out.println(authentication.getPrincipal());
            
            User owner = userRepository.findByEmail(authentication.getName());
            String imageUrl = firebaseService.uploadFile(file, "product_images");
            System.out.println("------------------------------------------------------------" + imageUrl);
            product.setLender(owner);
            product.setProductImage(imageUrl);
            product.setAvailable(true);
            Product saved = productRepository.save(product);
            return ResponseEntity.ok().body(saved);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("ERROR WHILE ADDING YOUR PRODUCT, PLEASE TRY AGAIN AFTER SOME TIME.");
        }

    }

    // Add multiple products
    public List<Product> addProduct(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Product not Found!"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // delete product
    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        } else {
            return false;
        }
    }

    public List<Product> getNewlyAddedProductsByCategory(String category) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return productRepository.findNewlyAddedProductsByCategory(category, oneWeekAgo);
    }

    public List<Product> listProductsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        String email = authentication.getName();
        List<Product> response = productRepository.findByLenderEmail(email);
        return response;
    }

    // New Update Method
    public ResponseEntity<?> updateProduct(Long id, Product updatedProduct, MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();
            User owner = userRepository.findByEmail(userEmail);

            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Product not found with id: " + id));

            // Check if the authenticated user is the owner of the product
            if (!existingProduct.getLender().getId().equals(owner.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to update this product.");
            }

            // If a new image is provided, upload it and update the image URL
            if (file != null && !file.isEmpty()) {
                String imageUrl = firebaseService.uploadFile(file, "product_images");
                existingProduct.setProductImage(imageUrl);
            }

            // Update the product fields
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setRentalPrice(updatedProduct.getRentalPrice());
            existingProduct.setAvailable(updatedProduct.getAvailable());

            // Save the updated product
            Product savedProduct = productRepository.save(existingProduct);

            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while updating the product. Please try again later.");
        }
    }


    public ResponseEntity<?> changeAvailability(Long id, Boolean status) {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User owner = userRepository.findByEmail(userEmail);
    
        // Retrieve the product by ID
        Product item = productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Product not found with id: " + id));
    
        // Check if the authenticated user is the owner of the product
        if (!owner.getId().equals(item.getLender().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to update this product.");
        }
    
        // Update the available status and save the product
        item.setAvailable(status);
        productRepository.save(item);  // Save the updated product to the database
    
        return ResponseEntity.ok("Product availability updated successfully.");
    }
    
    
  //  public Product updateProduct(Long id, Product updatedProduct) {
  //      Optional<Product> existingProductOptional = productRepository.findById(id);

  //       if (existingProductOptional.isPresent()){
  //            Product existingProduct = existingProductOptional.get();
             //update fields
  //           existingProduct.setName(updatedProduct.getName());
  //           existingProduct.setDescription(updatedProduct.getDescription());
  //           existingProduct.setCategory(updatedProduct.getCategory());
  //           existingProduct.setRentalPrice(updatedProduct.getRentalPrice());
  //           existingProduct.setAvailable(updatedProduct.getAvailable());
  //           return productRepository.save(existingProduct);
  //       } else {
  //           throw new CustomException("Product not found with id: "+ id);
  //       }
  //  }

    public List<Product> getProductsByLenderEmail(String email) {
        return productRepository.findByLenderEmail(email);
    }
}