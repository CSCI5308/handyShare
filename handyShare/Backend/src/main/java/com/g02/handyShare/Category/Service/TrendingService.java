package com.g02.handyShare.Category.Service;

import com.g02.handyShare.Category.Entity.Trending;
import com.g02.handyShare.Category.Repository.TrendingRepository;
import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendingService {

    @Autowired
    private TrendingRepository repository;

    @Autowired
    private ProductRepository productRepository;

    public ResponseEntity<?> fetchTrendingsByCategory(String category) {
        List<Trending> response = repository.findByProductCategory(category);
        return ResponseEntity.ok(response);
    }

    // public Trending addToTrending(Long productId) {

    //     try{
    //         Product product = productRepository.findById(productId)
    //                 .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    //         Trending trending = new Trending();
    //         trending.setProduct(product);
    //         return repository.save(trending);
    //     }catch (Exception e){
    //         throw new RuntimeException("Something went wrong while adding the product to the trending table"+e);
    //     }

    // }
    public Trending addToTrending(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
            Trending trending = new Trending();
            trending.setProduct(product);
            return repository.save(trending);
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong while adding the product to the trending table: " + e.getMessage());
        }
    }
    
}
