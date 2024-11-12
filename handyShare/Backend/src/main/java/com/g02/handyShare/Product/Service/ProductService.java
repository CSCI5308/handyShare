package com.g02.handyShare.Product.Service;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private static final String PRODUCT_NOT_FOUND_MSG = "Product not found with id: ";
    private static final String UNAUTHORIZED_ACTION_MSG = "You are not authorized to update this product.";
    private static final String ERROR_ADDING_PRODUCT_MSG = "Error while adding your product, please try again later.";
    private static final String ERROR_UPDATING_PRODUCT_MSG = "Error while updating the product. Please try again later.";

    @Autowired
    private ProductRepository productRepository;

    private FirebaseService firebaseService;

    @Autowired
    public void setFirebaseService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @Autowired
    private UserRepository userRepository;

    // Helper method for getting the current authenticated user
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public ResponseEntity<?> addProduct(Product product, MultipartFile file) {
        try {
            User owner = getAuthenticatedUser();
            String imageUrl = firebaseService.uploadFile(file, "product_images");
            logger.info("Image URL: " + imageUrl);

            product.setLender(owner);
            product.setProductImage(imageUrl);
            product.setAvailable(true);

            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            logger.error("Error while uploading the product image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ERROR_ADDING_PRODUCT_MSG);
        }
    }

    public List<Product> addProduct(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND_MSG + id));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean deleteProduct(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    public List<Product> getNewlyAddedProductsByCategory(String category) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return productRepository.findNewlyAddedProductsByCategory(category, oneWeekAgo);
    }

    public List<Product> listProductsForUser() {
        String email = getAuthenticatedUser().getEmail();
        return productRepository.findByLenderEmail(email);
    }

    public ResponseEntity<?> updateProduct(Long id, Product updatedProduct, MultipartFile file) {
        try {
            User owner = getAuthenticatedUser();
            Product existingProduct = productRepository.findById(id)
                    .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND_MSG + id));

            if (!existingProduct.getLender().getId().equals(owner.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(UNAUTHORIZED_ACTION_MSG);
            }

            if (file != null && !file.isEmpty()) {
                String imageUrl = firebaseService.uploadFile(file, "product_images");
                existingProduct.setProductImage(imageUrl);
            }

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setRentalPrice(updatedProduct.getRentalPrice());
            existingProduct.setAvailable(updatedProduct.getAvailable());

            Product savedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            logger.error("Error while updating the product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ERROR_UPDATING_PRODUCT_MSG);
        }
    }

    public ResponseEntity<?> changeAvailability(Long id, Boolean status) {
        User owner = getAuthenticatedUser();
        Product item = productRepository.findById(id)
                .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND_MSG + id));

        if (!owner.getId().equals(item.getLender().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(UNAUTHORIZED_ACTION_MSG);
        }

        item.setAvailable(status);
        productRepository.save(item);

        return ResponseEntity.ok("Product availability updated successfully.");
    }

    public List<Product> getProductsByLenderEmail(String email) {
        return productRepository.findByLenderEmail(email);
    }
}
