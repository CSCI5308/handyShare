package com.g02.handyShare.Product.Repository;
import com.g02.handyShare.Product.Entity.Product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.createdDate >= :oneWeekAgo")
    List<Product> findNewlyAddedProductsByCategory(@Param("category") String category, @Param("oneWeekAgo") LocalDateTime oneWeekAgo);

}
