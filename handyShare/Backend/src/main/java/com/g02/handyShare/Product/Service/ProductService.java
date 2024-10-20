package com.g02.handyShare.Product.Service;

import com.g02.handyShare.Product.Entity.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Add multiple products
    public List<Product> addProduct(List<Product> products) {
        return productRepository.saveAll(products);
    }

    //update product entries
    public Product updateProduct(Long id, Product updatedProduct){
        Optional<Product> existingProductOptional=productRepository.findById(id);

        if (existingProductOptional.isPresent()){
            Product existingProduct=existingProductOptional.get();
            //update fields
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setRentalPrice(updatedProduct.getRentalPrice());
            existingProduct.setAvailable(updatedProduct.getAvailable());

            return productRepository.save(existingProduct);
        }else{
            throw new CustomException("Product not found with id: "+ id);
        }
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not Found!"));
    }

    //delete product
    public boolean deleteProduct(Long productId){
        if(productRepository.existsById(productId)){
            productRepository.deleteById(productId);
            return true;
        }else {
            return false;
        }
    }
}
