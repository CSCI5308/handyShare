package com.g02.handyShare.Product.Repository;
import com.g02.handyShare.Product.Model.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {
}
