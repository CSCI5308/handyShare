package com.g02.handyShare.Product.Service;

import com.g02.handyShare.Category.Entity.Category;
import com.g02.handyShare.Config.Firebase.FirebaseService;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import com.g02.handyShare.User.Entity.User;
import com.g02.handyShare.User.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
            Product tobeSaved = new Product();
            String imageUrl = firebaseService.uploadFile(file, "product_images");
            System.out.println("------------------------------------------------------------" + imageUrl);
            tobeSaved.setAvailable(true);
            tobeSaved.setCategory(product.getCategory());
            tobeSaved.setDescription(product.getDescription());
            tobeSaved.setProductImage(imageUrl);
            try {
                tobeSaved.setRentalPrice(Double.valueOf(product.getRentalPrice()));
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid price format");
            }
            tobeSaved.setName(product.getName());
            tobeSaved.setUser(owner);
            Product saved = productRepository.save(tobeSaved);
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
                .orElseThrow(() -> new RuntimeException("Product not Found!"));
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

    // update product entries
    // public Product updateProduct(Long id, Product updatedProduct){
    // Optional<Product> existingProductOptional=productRepository.findById(id);
    //
    // if (existingProductOptional.isPresent()){
    // Product existingProduct=existingProductOptional.get();
    // //update fields
    // existingProduct.setName(updatedProduct.getName());
    // existingProduct.setDescription(updatedProduct.getDescription());
    // existingProduct.setCategory(updatedProduct.getCategory());
    // existingProduct.setRentalPrice(updatedProduct.getRentalPrice());
    // existingProduct.setAvailable(updatedProduct.getAvailable());
    //
    // return productRepository.save(existingProduct);
    // }else{
    // throw new CustomException("Product not found with id: "+ id);
    // }
    // }

}
