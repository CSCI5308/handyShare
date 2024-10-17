package com.g02.handyShare.Product.Service;

import com.g02.handyShare.Product.Model.Product;
import com.g02.handyShare.Product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    //add product
    public Product addProduct(Product product){
        return productRepository.save(product);
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
            throw new RuntimeException("Product not found with id"+ id);
        }
    }

    //delete product
    public void deleteProduct(Long productId){
        productRepository.deleteById(productId);

    }
}
